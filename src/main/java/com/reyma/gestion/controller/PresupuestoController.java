package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.LineaPresupuestoService;
import com.reyma.gestion.service.PresupuestoService;
import com.reyma.gestion.service.SiniestroService;
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
	public String alta(@RequestBody PresupuestoDTO presupuestoDto ) {		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		Calendar cal = new GregorianCalendar();
		
		//ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
		//Converter<LineaPresupuestoDTO, LineaPresupuesto> converter = acsf.getLineaPresupuestoDTOToLineaPresupuestoConverter();
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError;
		
		Presupuesto pres = presupuestoService.findPresupuesto(presupuestoDto.getIdPresupuesto());
		if ( pres == null ){
			mensajeError = new MensajeErrorJson("No se ha podido encontrar el presupuesto para modificar");
			mensajeError.setTitulo("Error actualizando presupuesto");
			return serializer.exclude("class").serialize(mensajeError);
		} else {
			try {
				// campos generales de fecha, num pres y afectado
				cal.setTime( sdf.parse( presupuestoDto.getFechaPresupuesto() ) );
				pres.setPresFecha(cal);
				pres.setPresNumPresupuesto(presupuestoDto.getNumPresupuesto());
				AfectadoDomicilioSiniestro afectado = adsService.findAfectadoDomicilioSiniestro(presupuestoDto.getIdAfectado());
				pres.setPresAdsId(afectado);			
				// grabar factura
				presupuestoService.savePresupuesto(pres);
			} catch (ParseException e) {				
				mensajeError = new MensajeErrorJson("No se ha podido modificar la fecha del presupuesto");
				mensajeError.setTitulo("Error actualizando presupuesto");
				LOG.error("error parseando fecha:\n", e);
				return serializer.exclude("class").serialize(mensajeError);
			} catch (Exception e) {
				LOG.error("error grabando presupuesto:\n", e); 
			}			
		}		
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("exito", Boolean.TRUE);
		return serializer.exclude("class").serialize(res);
    }
		
	@ResponseBody
	@RequestMapping(value = "/{presId}", params = "eliminar", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")	
    public String delete(@PathVariable("presId") Integer idPresupuesto, Model uiModel) {
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
				
		Presupuesto presupuesto = presupuestoService.findPresupuesto(idPresupuesto);
		try {
			presupuesto.remove();
			MensajeExitoJson mensajeExito = new MensajeExitoJson("El presupuesto se ha eliminado con &eacute;xito");
			return serializer.exclude("class").serialize(mensajeExito);
		} catch (Exception e) {			
			LOG.error("error eliminando el presupuesto:\n", e);
			mensajeError = new MensajeErrorJson("Se ha producido un error inesperado elimianando el presupuesto");
			mensajeError.setTitulo("Error eliminando presupuesto");
		}
		return serializer.exclude("class").serialize(mensajeError);
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
