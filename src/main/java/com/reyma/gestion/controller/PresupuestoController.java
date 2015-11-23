package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.LineaPresupuestoService;
import com.reyma.gestion.service.PresupuestoService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.ui.LineaPresupuestoDTO;
import com.reyma.gestion.ui.MensajeErrorJson;
import com.reyma.gestion.ui.MensajeExitoJson;
import com.reyma.gestion.ui.PresupuestoDTO;

import flexjson.JSONSerializer;

@RequestMapping("/presupuestos")
@Controller
public class PresupuestoController {

	@Autowired
    PresupuestoService presupuestoService;

	@Autowired
    LineaPresupuestoService lineaPresupuestoService;

	@Autowired
    SiniestroService siniestroService;
	
	@Autowired
	AfectadoDomicilioSiniestroService adsService;

	private static final Logger LOG = Logger.getLogger(PresupuestoController.class);
	
	@ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
	public String alta(@RequestBody PresupuestoDTO presupuestoDto) {
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
		Converter<LineaPresupuestoDTO, LineaPresupuesto> converter = acsf.
				getLineaPresupuestoDTOToLineaPresupuestoConverter();
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError;		
		try {
			Presupuesto presupuesto = cargarDatosGeneralesPres(presupuestoDto);				
			// grabar presupuesto
			presupuestoService.savePresupuesto(presupuesto);
			// lineas
			LineaPresupuestoDTO[] lineas = presupuestoDto.getLineasPresupuesto();
			LineaPresupuesto aux;
			for (LineaPresupuestoDTO linea : lineas ) {
				aux = converter.convert(linea);
				aux.setLinPresId(presupuesto);
				lineaPresupuestoService.saveLineaPresupuesto(aux);
			}								
		} catch (ParseException e) {				
			mensajeError = new MensajeErrorJson("No se ha podido modificar la fecha del presupuesto");
			mensajeError.setTitulo("Error actualizando presupuesto");
			LOG.error("error parseando fecha:\n", e);
			return serializer.exclude("class").serialize(mensajeError);
		} catch (Exception e) {
			mensajeError = new MensajeErrorJson("No se ha podido garbar el presupuesto");
			mensajeError.setTitulo("Error guardando presupuesto");
			LOG.error("error grabando presupuesto:\n", e); 
			return serializer.exclude("class").serialize(mensajeError);			
		}	
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("exito", Boolean.TRUE);
		return serializer.exclude("class").serialize(res);
    }
	
	@ResponseBody
    @RequestMapping(value = "/actualizar", method = RequestMethod.POST)
	public String actualizar(@RequestBody PresupuestoDTO presupuestoDto) {
		
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
		Converter<LineaPresupuestoDTO, LineaPresupuesto> converter = acsf.
				getLineaPresupuestoDTOToLineaPresupuestoConverter();
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError;		
		try {			
			Presupuesto actual = presupuestoService.findPresupuesto(presupuestoDto.getIdPresupuesto());			
			Presupuesto modificado = cargarDatosGeneralesPres(presupuestoDto);		
			// actualizar datos generales presupuesto
			actual.setPresAdsId( modificado.getPresAdsId() );
			actual.setPresFecha( modificado.getPresFecha() );
			actual.setPresNumPresupuesto( modificado.getPresNumPresupuesto() ); 
			Presupuesto nuevo = presupuestoService.updatePresupuesto(actual);						
			// borrar lineas anteriores y crear nuevas			
			nuevo.getLineasPresupuesto().clear();
			LineaPresupuestoDTO[] lineas = presupuestoDto.getLineasPresupuesto();
			LineaPresupuesto aux;
			for (LineaPresupuestoDTO linea : lineas ) {
				aux = converter.convert(linea);
				aux.setLinPresId(nuevo);
				lineaPresupuestoService.saveLineaPresupuesto(aux);
				nuevo.getLineasPresupuesto().add(aux);
			}							
		} catch (ParseException e) {				
			mensajeError = new MensajeErrorJson("No se ha podido modificar la fecha del presupuesto");
			mensajeError.setTitulo("Error actualizando presupuesto");
			LOG.error("error parseando fecha:\n", e);
			return serializer.exclude("class").serialize(mensajeError);
		} catch (Exception e) {
			mensajeError = new MensajeErrorJson("No se ha podido grabar el presupuesto");
			mensajeError.setTitulo("Error guardando presupuesto");
			LOG.error("error grabando presupuesto:\n", e); 
			return serializer.exclude("class").serialize(mensajeError);			
		}	
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("exito", Boolean.TRUE);
		return serializer.exclude("class").serialize(res);
    }

	private Presupuesto cargarDatosGeneralesPres(PresupuestoDTO presupuestoDto)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		Calendar cal = new GregorianCalendar();
		Presupuesto presupuesto = new Presupuesto();
		// siniestro correspondiente
		Siniestro siniestro = siniestroService.findSiniestro(presupuestoDto.getIdSiniestro());
		presupuesto.setPresSinId(siniestro);
		// campos generales de fecha, num pres y afectado
		presupuesto.setPresNumPresupuesto(presupuestoDto.getNumPresupuesto());
		if ( StringUtils.isNotBlank(presupuestoDto.getFechaPresupuesto())) {
			cal.setTime( sdf.parse( presupuestoDto.getFechaPresupuesto() ) );
			presupuesto.setPresFecha(cal);
		}			
		if ( presupuestoDto.getIdAfectado() != null ) {
			AfectadoDomicilioSiniestro afectado = adsService.
					findAfectadoDomicilioSiniestro(presupuestoDto.getIdAfectado());
			presupuesto.setPresAdsId(afectado);			
		}
		return presupuesto;
	}
		
	@ResponseBody
	@RequestMapping(value = "/{presId}", params = "eliminar", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")	
    public String delete(@PathVariable("presId") Integer idPresupuesto, Model uiModel) {
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
				
		Presupuesto presupuesto = presupuestoService.findPresupuesto(idPresupuesto);
		try {
			presupuesto.remove();
			MensajeExitoJson mensajeExito = new MensajeExitoJson("El presupuesto se ha eliminado con &eacute;xito", true);
			return serializer.exclude("class").serialize(mensajeExito);
		} catch (Exception e) {			
			LOG.error("error eliminando el presupuesto:\n", e);
			mensajeError = new MensajeErrorJson("Se ha producido un error inesperado eliminando el presupuesto");
			mensajeError.setTitulo("Error eliminando presupuesto");
		}
		return serializer.exclude("class").serialize(mensajeError);
    }
	
	@RequestMapping(value = "/{presId}", params = "pdf", method = RequestMethod.GET)
	public ModelAndView generarPresupuestoPDF(@PathVariable("presId") Integer idPresupuesto, Model uiModel) {
				
		Presupuesto presupuesto = presupuestoService
				.findPresupuesto(idPresupuesto);
				
		return new ModelAndView("pdfView", "presupuesto", presupuesto);
	}
		
	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("presupuesto_presfecha_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Presupuesto presupuesto) {
        uiModel.addAttribute("presupuesto", presupuesto);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lineaspresupuesto", lineaPresupuestoService.findAllLineasPresupuesto());
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
