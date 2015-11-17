package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.LineaPresupuestoService;
import com.reyma.gestion.service.PresupuestoService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.ui.MensajeErrorJson;
import com.reyma.gestion.ui.MensajeErrorValidacionJson;
import com.reyma.gestion.ui.MensajeExitoJson;

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
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Presupuesto presupuesto, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, presupuesto);
            return "presupuestos/create";
        }
        uiModel.asMap().clear();
        presupuestoService.savePresupuesto(presupuesto);
        return "redirect:/presupuestos/" + encodeUrlPathSegment(presupuesto.getPresId().toString(), httpServletRequest);
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String alta(@Valid Presupuesto presupuesto, BindingResult bindingResult, Model uiModel, HttpServletRequest request) {		
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
					Converter<String, List<LineaPresupuesto>> converter = acsf.getLineaPresupuestoJsonStringToLineasPresupuestoConverter();
					List<LineaPresupuesto> lineas = converter.convert( request.getParameter("presFac") );
					// TODO: generar el numero de factura
					// grabamos presupuesto
					presupuestoService.savePresupuesto(presupuesto);
					for (LineaPresupuesto lineaPresupuesto : lineas) {						
						lineaPresupuesto.setLinPresId(presupuesto);
						lineaPresupuestoService.saveLineaPresupuesto(lineaPresupuesto);
					}					
					Map<String, Boolean> res = new HashMap<String, Boolean>();
					res.put("recargar", Boolean.TRUE);
					return serializer.exclude("class").serialize(res);				
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
