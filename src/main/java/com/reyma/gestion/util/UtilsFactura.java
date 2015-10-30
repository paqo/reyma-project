package com.reyma.gestion.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.ui.FacturaPdfDTO;

public class UtilsFactura {
		
	public static String generarHTMLParaFactura(
			HttpServletRequest request, HttpServletResponse response,
			Factura factura) {
		CharArrayWriterResponse resp = obtenerHTMLStringDesdeJSP(request, response, factura, false);
		return resp.getOutput();		
	}
	
	public static String generarHTMLParaPresupuesto(
			HttpServletRequest request, HttpServletResponse response,
			Factura factura) {
		CharArrayWriterResponse resp = obtenerHTMLStringDesdeJSP(request, response, factura, true);
		return resp.getOutput();		
	}
	
	private static CharArrayWriterResponse obtenerHTMLStringDesdeJSP (
			HttpServletRequest request, HttpServletResponse response,
			Factura factura, boolean presupuesto) {
		CharArrayWriterResponse customResponse  = new CharArrayWriterResponse(response);
	    try {
	    	
	    	FacturaPdfDTO pdf = new FacturaPdfDTO();	    	
	    	Siniestro siniestro = factura.getFacSinId();
	    		    	
	    	// 1.- datos factura:
	    	setDatosFactura(pdf, factura, siniestro);
	    	
	    	// 2.- lineas factura	    	
	    	setLineasFactura(pdf, factura);
	    	
	    	// 3.- datos reyma
	    	setDatosReyma(pdf);	   
	    	
	    	if ( presupuesto ){
	    		request.setAttribute("presupuesto", Boolean.TRUE);
	    		List<String[]> subtotales = comprobarImporteCeroEnConceptos(pdf);
	    		if ( subtotales.size() > 0 ){
	    			request.setAttribute("gremiosConCero", Boolean.TRUE);
	    			request.setAttribute("subtotales", subtotales);
	    			request.setAttribute("totalGremios", totalizarSubTotales(subtotales));
	    		}
	    	}
	    	
	    	request.setAttribute("factura", pdf);
	    	request.setAttribute("servidor", obtenerServidor(request));

			request.getRequestDispatcher("/WEB-INF/views/facturas/generarfactura.jsp").forward(request, customResponse);
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customResponse;
	}	

	private static BigDecimal totalizarSubTotales(List<String[]> subtotales) {		
		BigDecimal res = new BigDecimal("0");
		for (String[] subtotal : subtotales) {			
			res = res.add( new BigDecimal(subtotal[4]) ); 
		}
		return res.setScale(2, RoundingMode.FLOOR);
	}

	private static List<String[]> comprobarImporteCeroEnConceptos(FacturaPdfDTO facPdf) {
		
		List<String[]> res = new ArrayList<String[]>();
		Map<String, List<LineaFactura>> mapaLineas = facPdf.getLineasFactura();
		
		Iterator<String> it = mapaLineas.keySet().iterator();
		String gremio;
		List<LineaFactura> lineas;
		int cont;
		String [] subtotal = null;
		while ( it.hasNext() ){
			gremio = it.next();
			lineas = mapaLineas.get(gremio);
			cont = 0;
			for (LineaFactura linea : lineas) { //linea.getLinImporte().unscaledValue() == BigInteger.valueOf(0)
				if ( !linea.getLinImporte().unscaledValue().equals(BigInteger.ZERO) ) {
					cont++;
					subtotal = new String[]{"", // el concepto queda a vacio
									 linea.getLinImporte().toString(), 
									 linea.getLinIvaId().getIvaValor() + "%", 
									 obtenerIvaAplicado(linea), 
									 obtenerSubtotal(linea)
									 };
				}				
			}
			if ( cont == 1 && lineas.size() > 1 ) {
				// para ese gremio, generamos linea de subtotal
				if (subtotal != null) { 
					res.add(subtotal);
				} else { // no deberia pasar
					res.add( new String[]{"", "", "", "", ""} );					
				}				
			}
		}		
		return res;
	}
	
	private static String obtenerIvaAplicado(LineaFactura linea) {		
		BigDecimal res = new BigDecimal(
								(linea.getLinImporte().doubleValue() * 
								 linea.getLinIvaId().getIvaValor()) / 100 );
		return res.setScale(2, RoundingMode.FLOOR).toString();
	}
	
	public static String obtenerSubtotal(LineaFactura linea) {		
		BigDecimal res = new BigDecimal(
								linea.getLinImporte().doubleValue() + 
								(linea.getLinImporte().doubleValue() * linea.getLinIvaId().getIvaValor()) / 100 );
		return res.setScale(2, RoundingMode.FLOOR).toString();
	}

	public static void generarPDFDesdeHTML(HttpServletResponse response,
			Factura factura, String facturaHtml, String nombre) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			StringReader contentReader = new StringReader(facturaHtml);
			InputSource source = new InputSource(contentReader);
			Document xhtmlContent = builder.parse(source);
			
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(xhtmlContent,"");
			renderer.layout();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre + ".pdf\"");			
			OutputStream browserStream = response.getOutputStream();
			renderer.createPDF(browserStream);
			browserStream.flush();
			browserStream.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private static void setDatosReyma(FacturaPdfDTO pdf) {
		ResourceBundleMessageSource mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("reymasur");
		
		pdf.setNombreR(
				mensajes.getMessage("nombre", null, Locale.getDefault()) );
		pdf.setDomicilioR(
				mensajes.getMessage("direccion", null, Locale.getDefault()) );
		pdf.setLocalidadR(					
				mensajes.getMessage("localidad", null, Locale.getDefault()) );
		pdf.setCpR(					
				mensajes.getMessage("cp", null, Locale.getDefault()) );
		pdf.setNifR(
				mensajes.getMessage("cif", null, Locale.getDefault()) );
		pdf.setTelefonoR(
				mensajes.getMessage("telefono", null, Locale.getDefault()) );
		pdf.setFaxR(
				mensajes.getMessage("fax", null, Locale.getDefault()) );
		pdf.setUrlR(
				mensajes.getMessage("url", null, Locale.getDefault()) );
		pdf.setEmailR(
				mensajes.getMessage("email", null, Locale.getDefault()) );
		pdf.setNombreCortoR(
				mensajes.getMessage("nombreCorto", null, Locale.getDefault()) );		
	}
	
	private static void setLineasFactura(FacturaPdfDTO pdf, Factura factura) {
		
		List<LineaFactura> _lineasFac = new ArrayList<LineaFactura>(factura.getLineaFacturas());		
		Collections.sort(_lineasFac, new Comparator<LineaFactura>() {			
			@Override
			public int compare(LineaFactura linea1, LineaFactura linea2) {
				if ( linea1.getLinId() > linea2.getLinId() ) {
					return 1;
				} else if ( linea1.getLinId() < linea2.getLinId() ) {
					return -1;
				} else {
					return 0;
				}				
			}
		});
		
		Map<String, List<LineaFactura>> mapa = new LinkedHashMap<String, List<LineaFactura>>();
		Iterator<LineaFactura> it = _lineasFac.iterator();
				
		LineaFactura lineaFactura;
		List<LineaFactura> listaLineasMismoOfi; 
		while ( it.hasNext() ) {
			lineaFactura = it.next();
			listaLineasMismoOfi = (List<LineaFactura>) mapa.get(lineaFactura.getLinOficioId().getOfiDescripcion());
			if ( listaLineasMismoOfi == null ){
				listaLineasMismoOfi = new ArrayList<LineaFactura>();
				mapa.put(lineaFactura.getLinOficioId().getOfiDescripcion(), listaLineasMismoOfi);					
			}
			listaLineasMismoOfi.add(lineaFactura);
		}	    	
		pdf.setLineasFactura(mapa);
	}

	private static void setDatosFactura(FacturaPdfDTO pdf, Factura factura,
			Siniestro siniestro) {
		AfectadoDomicilioSiniestro ads = factura.getFacAdsId();
		pdf.setNumFactura(factura.getFacNumFactura());
		pdf.setFechaFactura( Fechas.formatearFechaDDMMYYYY(
				factura.getFacFecha().getTime()) );
		pdf.setFechaEncargo( Fechas.formatearFechaDDMMYYYY(
								siniestro.getSinFechaEncargo().getTime()) );
		if ( siniestro.getSinFechaFin() != null ){
			pdf.setFechaFin( Fechas.formatearFechaDDMMYYYY(
					siniestro.getSinFechaFin().getTime()) );
		} else { 
			// se deja a vacio si no hay fecha de fin
			pdf.setFechaFin( "" );
		}
		
		pdf.setDomicilio( ads.getAdsDomId().getDomDireccion() + ", " 
						+ ads.getAdsDomId().getDomMunId().getMunDescripcion() + ", (" 
						+ ads.getAdsDomId().getDomProvId().getPrvDescripcion() + ")"
		);
		pdf.setCp( ads.getAdsDomId().getDomCp().toString() );
		pdf.setNif( ads.getAdsPerId().getPerNif() );
		pdf.setNombre( ads.getAdsPerId().getPerNombre() );
		pdf.setNumEncargo( siniestro.getSinNumero() );
		pdf.setNumFactura(factura.getFacNumFactura());
	}
		
	private static String obtenerServidor(HttpServletRequest request) {
		String serv = request.getServerName() + ":" + request.getServerPort();
		String protocolo = "http://";
		if ( request.getProtocol().toUpperCase().startsWith("HTTPS") ) {
			protocolo = "https://";
		} 
		return protocolo + serv;
	}
}
