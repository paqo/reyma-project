package com.reyma.gestion.ui.pdf;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.util.UtilsPDF;

public class PresupuestoPDF extends AbstractPdfView {
	
	private CabeceraPie cabeceraPie;
	private Logger logger = Logger.getLogger( CabeceraPie.class );
	
	@Override
	protected void prepareWriter(Map<String, Object> modelo, PdfWriter writer,
			HttpServletRequest request) throws DocumentException {
		
		Presupuesto presupuesto = (Presupuesto) modelo.get("presupuesto");
		cabeceraPie = new CabeceraPie(presupuesto.getPresFecha());
		
		writer.setBoxSize("art", new Rectangle(36, 40, 559, 788));
		writer.setPageEvent( cabeceraPie );		
	}
	
	@Override
	protected void buildPdfDocument(Map<String, Object> modelo, Document document,
			PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Presupuesto presupuesto = (Presupuesto) modelo.get("presupuesto");		
		List<LineaPresupuesto> lineas = presupuesto.getLineasPresupuesto();
		// ordenar las lineas por id
		Collections.sort(lineas, new Comparator<LineaPresupuesto>() {
			@Override
			public int compare(LineaPresupuesto linea1, LineaPresupuesto linea2) {				
				return linea1.getLinId() < linea2.getLinId()? -1 :
						linea1.getLinId() > linea2.getLinId() ? 1 : 0;				
			}
		});
		
    
		/* -- DATOS DE PRUEBA:
		 * Presupuesto presupuesto = new Presupuesto();
		presupuesto.setPresNumPresupuesto("12345678");
		presupuesto.setPresFecha(new GregorianCalendar());
		
		AfectadoDomicilioSiniestro ads = new AfectadoDomicilioSiniestro();
		
		Domicilio domic = new Domicilio();
		domic.setDomCp(45687);
		domic.setDomDireccion("C/ de la polka nº 4, P6 1D");
		ads.setAdsDomId(domic);
		Persona persona = new Persona();
		persona.setPerNif("78662301");
		persona.setPerNombre("My Sister Cousin");
		ads.setAdsPerId(persona);*/
		
        // step 4
        PdfPTable tabla = getTablaDatosAfectado(presupuesto);
        tabla.setSpacingBefore(15);
        tabla.setSpacingAfter(10);   
        document.add(tabla);                
        // step 5
        PdfPTable tablaLineas = getTablaLineas(lineas);
        document.add(tablaLineas);        
        // step 6
        cabeceraPie.setContentLeft(false);
        document.close();
        System.out.println("terminado!");
		
	}

	private PdfPTable getTablaLineas(List<LineaPresupuesto> lineas) {
		PdfPTable tabLineas = new PdfPTable(new float[] { 
				5, // descripcion
				2, // base imp
				1, // porcentaje IVA
				2, // cant. iva
				3  // subtotal 
		});        
		tabLineas.setWidthPercentage(100f);
		// cabecera columnas        
		tabLineas.getDefaultCell().setPadding(5); 
		tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		tabLineas.getDefaultCell().setColspan(1);        
		tabLineas.getDefaultCell().setBorderColor(Color.BLACK);        
		tabLineas.getDefaultCell().setBorderWidth(0.5f);
		tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#20A627"));
		tabLineas.addCell(new Phrase("Descripcion", UtilsPDF.FUENTE_CABECERA_COLS));        
		tabLineas.addCell(new Phrase("Base", UtilsPDF.FUENTE_CABECERA_COLS));
		tabLineas.addCell(new Phrase("%", UtilsPDF.FUENTE_CABECERA_COLS));
		tabLineas.addCell(new Phrase("IVA", UtilsPDF.FUENTE_CABECERA_COLS));
		tabLineas.addCell(new Phrase("Subtotal", UtilsPDF.FUENTE_CABECERA_COLS));		
		// establecer linea de cabecera
		tabLineas.setHeaderRows(1);		
		
		DecimalFormat FORMATEADOR_DECIMALES = (DecimalFormat) DecimalFormat.getInstance(new Locale("es", "ES"));		
		FORMATEADOR_DECIMALES.setMaximumFractionDigits(2);
		FORMATEADOR_DECIMALES.setRoundingMode(RoundingMode.HALF_UP);
		
		double 	porcaplicado,
		subtotal, 
		totalFactura = 0.0;	
		for (LineaPresupuesto linea : lineas) {					
			if ( linea.getLinOficioId() != null ){ // cabecera con el oficio
				tabLineas.getDefaultCell().setBorder(
						Rectangle.TOP|Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
				tabLineas.getDefaultCell().setBorderColor(Color.BLACK);
				tabLineas.getDefaultCell().setPadding(5); 
				tabLineas.getDefaultCell().setUseAscender(true);
				tabLineas.getDefaultCell().setUseDescender(true);
				tabLineas.getDefaultCell().setColspan(5);
				tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#A6EDA9")); 
				tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				tabLineas.addCell( linea.getLinOficioId().getOfiDescripcion() );
				tabLineas.getDefaultCell().setBackgroundColor(null);  
				tabLineas.getDefaultCell().setColspan(1);
			} else { // linea
				porcaplicado= (linea.getLinIvaId().getIvaValor() * linea.getLinImporte().doubleValue())/100; 
				subtotal = linea.getLinImporte().doubleValue() + 
						(linea.getLinIvaId().getIvaValor() * linea.getLinImporte().doubleValue())/100;				
				tabLineas.getDefaultCell().setBorderColor(Color.BLACK);
				tabLineas.getDefaultCell().setBorder(Rectangle.LEFT);
				tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				tabLineas.addCell(linea.getLinConcepto()); // concepto
				tabLineas.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				tabLineas.addCell(linea.getLinImporte().toString());  // importe			
				tabLineas.addCell(String.valueOf(linea.getLinIvaId().getIvaValor()) + "%"); // porc iva				
				//tabLineas.addCell(String.valueOf(porcaplicado)); // iva
				tabLineas.addCell(FORMATEADOR_DECIMALES.format(porcaplicado)); // iva				
				tabLineas.getDefaultCell().setBorder(Rectangle.RIGHT);
				//tabLineas.addCell(String.valueOf(subtotal));
				tabLineas.addCell(FORMATEADOR_DECIMALES.format(subtotal));
				totalFactura += subtotal;
			}			
		}    
		// total
		tabLineas.getDefaultCell().setBorder(Rectangle.TOP|Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
		tabLineas.getDefaultCell().setPadding(3);
		tabLineas.getDefaultCell().setUseAscender(true);
		tabLineas.getDefaultCell().setUseDescender(true);
		tabLineas.getDefaultCell().setColspan(4);
		tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#20A627")); 
		tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabLineas.addCell("TOTAL: ");
		tabLineas.getDefaultCell().setColspan(1); 
		tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		//tabLineas.addCell(String.valueOf(totalFactura));
		tabLineas.addCell(FORMATEADOR_DECIMALES.format(totalFactura));
		return tabLineas;
	}

	private PdfPTable getTablaDatosAfectado(Presupuesto presupuesto) {
		PdfPTable tabDatosAfectados = new PdfPTable(new float[] { 1, 1});
		
		tabDatosAfectados.setWidthPercentage(100f);
		
		Paragraph datosTitulo = new Paragraph();		
		datosTitulo.add(new Chunk("PRESUPUESTO", UtilsPDF.FUENTE_CABECERA_AFECTADOS_TITULO));
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE);
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(new Chunk("Núm. presupuesto: " + presupuesto.getPresNumPresupuesto()));
		
    	tabDatosAfectados.getDefaultCell().setBorder(Rectangle.NO_BORDER);		
    	tabDatosAfectados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT|Element.ALIGN_TOP);
    	tabDatosAfectados.addCell(datosTitulo);
    	
    	tabDatosAfectados.getDefaultCell().setPadding(5);
    	tabDatosAfectados.getDefaultCell().setBorderWidth(1);
    	tabDatosAfectados.getDefaultCell().setBorder(Rectangle.TOP|Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
    	tabDatosAfectados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    	
    	String nombre = null, direccion = null, codigoPostal = null, nif = null;
    	try {
			Paragraph datosAfectado = new Paragraph();
			nombre = presupuesto.getPresAdsId().getAdsPerId().getPerNombre();
			direccion = presupuesto.getPresAdsId().getAdsDomId().getDomDireccion();
			codigoPostal = presupuesto.getPresAdsId().getAdsDomId().getDomCp().toString();
			nif = presupuesto.getPresAdsId().getAdsPerId().getPerNif();
			datosAfectado.add(new Chunk(nombre)); //nombre
			datosAfectado.add(Chunk.NEWLINE); 
			datosAfectado.add(new Chunk(direccion)); // direccion
			datosAfectado.add(Chunk.NEWLINE);  
			datosAfectado.add(new Chunk(codigoPostal)); // codigo postal
			datosAfectado.add(Chunk.NEWLINE);  
			datosAfectado.add(new Chunk(nif)); //NIF    	
			tabDatosAfectados.addCell(datosAfectado);
		} catch (Exception e) {
			logger.error("No se ha podido obtener la informacion del afectado:"
					+ "\nnombre: " + nombre
					+ "\ndireccion: " + direccion
					+ "\ncodigoPostal: " + codigoPostal
					+ "\nnif: " + nif + "\n", e);
		}    	
		return tabDatosAfectados;
	}
	
}
