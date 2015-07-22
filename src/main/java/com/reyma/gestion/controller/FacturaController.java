package com.reyma.gestion.controller;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;
import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.LineaFacturaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.ui.FacturaDTO;
import com.reyma.gestion.ui.FacturaPdfDTO;
import com.reyma.gestion.ui.LineaFacturaDTO;
import com.reyma.gestion.ui.MensajeErrorJson;
import com.reyma.gestion.ui.MensajeErrorValidacionJson;
import com.reyma.gestion.ui.MensajeExitoJson;
import com.reyma.gestion.util.CharArrayWriterResponse;

import flexjson.JSONSerializer;

@RequestMapping("/facturas")
@Controller
public class FacturaController {

	@Autowired
    FacturaService facturaService;

	@Autowired
    LineaFacturaService lineaFacturaService;

	@Autowired
    SiniestroService siniestroService;

	private static final Logger LOG = Logger.getLogger(FacturaController.class);
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Factura factura, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, factura);
            return "facturas/create";
        }
        uiModel.asMap().clear();
        facturaService.saveFactura(factura);
        return "redirect:/facturas/" + encodeUrlPathSegment(factura.getFacId().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String alta(@Valid Factura factura, BindingResult bindingResult, Model uiModel, HttpServletRequest request) {		
		List<FieldError> errores = new ArrayList<FieldError>();	
		errores = UtilsValidacion.getErroresValidacion(bindingResult.getFieldErrors());
		
		MensajeErrorValidacionJson mensajeError = null;
		JSONSerializer serializer = new JSONSerializer();
		
		if ( errores.size() == 0 ){
			if ( bindingResult.getFieldErrors().size() > 0 ){				
				mensajeError =  new MensajeErrorValidacionJson( bindingResult.getFieldErrors() );				
			} else {
				try {					
					ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
					Converter<String, List<LineaFactura>> converter = acsf.getLineaFacturaJsonStringToLineasFacturaConverter();
					List<LineaFactura> lineas = converter.convert( request.getParameter("linFac") );
					String msgValidacion = UtilsValidacion.facturaTieneLineasValidas(lineas);
					if ( msgValidacion != null ){
						mensajeError = new MensajeErrorValidacionJson( msgValidacion );						
					} else {
						// TODO: generar el numero de factura
						// grabamos factura
						facturaService.saveFactura(factura);
						for (LineaFactura lineaFactura : lineas) {						
							lineaFactura.setLinFacId(factura);
							lineaFacturaService.saveLineaFactura(lineaFactura);
						}					
						Map<String, Boolean> res = new HashMap<String, Boolean>();
						res.put("recargar", Boolean.TRUE);
						return serializer.exclude("class").serialize(res);
					}					
				} catch (Exception e) {						
					//TODO: que cuando falle en lineas, borre la factura
					e.printStackTrace();
				}
			}
		} else {
			// tiene errores de validacion
			mensajeError = new MensajeErrorValidacionJson(errores);
		}
		mensajeError.setRecargar(false);
		return serializer.exclude("class").serialize(mensajeError);
    }	
	
	@RequestMapping(value = "/generar/{objectId}", method = RequestMethod.GET)
	public String generateReport(
	        @PathVariable("objectId") Integer objectId, 
	        HttpServletRequest request, 
	        HttpServletResponse response) {
		
		CharArrayWriterResponse customResponse  = new CharArrayWriterResponse(response);
	    try {
	    	
	    	FacturaPdfDTO pdf = new FacturaPdfDTO();
	    	
	    	Factura factura = facturaService.findFactura( objectId );
	    	// datos factura
	    	pdf.setNumFactura(factura.getFacNumFactura());
	    	//TODO: el resto...
	    	
	    	// lineas factura
	    	//1.- obtener lineas de factura ordenadas por oficio
	    	Set<LineaFactura> _lineasFac = facturaService.findFactura( objectId ).getLineaFacturas();	    	
	    	SortedSet<LineaFactura> lineasFac =new TreeSet<LineaFactura>(new Comparator<LineaFactura>(){
	    	    public int compare(LineaFactura a, LineaFactura b){
	    	        return  a.getLinOficioId().getOfiId().compareTo(b.getLinOficioId().getOfiId());
	    	    }
	    	});
	    	
	    	Iterator<LineaFactura> it = _lineasFac.iterator();
	    	LineaFactura lineaFactura;
	    	while ( it.hasNext() ) {
				lineaFactura = it.next();
				lineasFac.add(lineaFactura);
			}
	    	
	    	//2.- crear mapa: oficio -> lista de lineas
	    	
	    	Map<String, List<LineaFactura>> mapa = new HashMap<String, List<LineaFactura>>();
	    	
	    	// acabar el bucle
	    	acabar
	    	List<LineaFactura> aux = new ArrayList<LineaFactura>();
	    	String ofiActual = "", ofiAnt = "";
	    	for (LineaFactura linea : lineasFac) {
	    		aux.add(linea);
	    		ofiActual = linea.getLinOficioId().getOfiDescripcion();
	    		if ( !ofiActual.equals(ofiAnt) ){
	    			mapa.put(ofiActual, aux);		
	    			aux = new ArrayList<LineaFactura>();
	    			ofiAnt = ofiActual;
	    		} 	    		
			}
	    	
	    	request.setAttribute("factura", pdf);

			request.getRequestDispatcher("/WEB-INF/views/facturas/generarfactura.jsp").forward(request, customResponse);
			
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	    String facturaHtml = customResponse.getOutput();
	    
	    System.out.println(facturaHtml);
	    
	    
	    generarPDF(facturaHtml);
	    
	    //System.out.println(String.format("La salida es %s", facturaHtml));

	    // TODO: mostrar el pdf es así?
	    // response.setContentType("application/pdf");
	    // response.getOutputStream().write(...);
	    
	    return "busquedas/inicio";

	}
	
	private void generarPDF(String htmlStr) {		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//Document doc = builder.parse(new StringBufferInputStream(htmlStr));
			
			Document doc = builder.parse( new ByteArrayInputStream(htmlStr.getBytes("UTF-8")) );

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(doc, null);

			String outputFile = "C:/temp/test.pdf";
			OutputStream os = new FileOutputStream(outputFile);
			renderer.layout();
			renderer.createPDF(os);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@ResponseBody
    @RequestMapping(value = "/actualizar", method = RequestMethod.POST)
	public String actualizar(@RequestBody FacturaDTO facturaDto ) {
		//TODO: hacer converter de FacturaDTO a Factura directamente
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		Calendar cal = new GregorianCalendar();
		
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
		Converter<LineaFacturaDTO, LineaFactura> converter = acsf.getLineaFacturaDTOToLineaFacturaConverter();
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError;
		
		Factura fac = facturaService.findFactura(facturaDto.getIdFactura());
		if ( fac == null ){
			mensajeError = new MensajeErrorJson("No se ha podido encontrar la factura para modificar");
			mensajeError.setTitulo("Error actualizando factura");
			return serializer.exclude("class").serialize(mensajeError);
		} else {
			try {
				cal.setTime( sdf.parse( facturaDto.getFechaFactura() ) );
				fac.setFacFecha(cal);
				fac.setFacNumFactura(facturaDto.getNumFactura());
				// actualizar lineas				
				Set<LineaFactura> lineasEditadas = new HashSet<LineaFactura>();
				LineaFactura lin;
				for (LineaFacturaDTO linea : facturaDto.getLineasFactura()) {
					lin = converter.convert(linea);
					lin.setLinFacId(fac);
					lineasEditadas.add(lin);
				}
				fac.getLineaFacturas().clear();
				fac.getLineaFacturas().addAll(lineasEditadas);			
				// actualizar factura
				facturaService.updateFactura(fac);
			} catch (ParseException e) {				
				mensajeError = new MensajeErrorJson("No se ha podido modificar la fecha de la factura");
				mensajeError.setTitulo("Error actualizando factura");
				LOG.error("error parseando fecha:\n", e);
				return serializer.exclude("class").serialize(mensajeError);
			} catch (Exception e) {
				LOG.error("error grabando factura:\n", e); 
			}			
		}		
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("exito", Boolean.TRUE);
		return serializer.exclude("class").serialize(res);
    }
		
	@ResponseBody
	@RequestMapping(value = "/{facId}", params = "eliminar", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")	
    public String delete(@PathVariable("facId") Integer idFactura, Model uiModel) {
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
				
		Factura factura = facturaService.findFactura(idFactura);
		try {		
			// funciona si tiene orphanRemoval=true, si no, habría que
			// iterar sobre las líneas de factura
			factura.remove();
			MensajeExitoJson mensajeExito = new MensajeExitoJson("La factura se ha eliminado con &eacute;xito");
			return serializer.exclude("class").serialize(mensajeExito);
		} catch (Exception e) {			
			e.printStackTrace();
			mensajeError = new MensajeErrorJson("Se ha producido un error inesperado elimianando la factura");
			mensajeError.setTitulo("Error eliminando factura");
		}
		return serializer.exclude("class").serialize(mensajeError);
    }
		
	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("factura_facfecha_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Factura factura) {
        uiModel.addAttribute("factura", factura);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lineafacturas", lineaFacturaService.findAllLineaFacturas());
        uiModel.addAttribute("siniestroes", siniestroService.findAllSiniestroes());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
