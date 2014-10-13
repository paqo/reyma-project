package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.controller.validators.AfectadoValidator;
import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoAfectacionService;
import com.reyma.gestion.ui.MensajeErrorValidacionJson;
import com.reyma.gestion.ui.MensajeExitoJson;

import flexjson.JSONSerializer;

@RequestMapping("/afectados")
@Controller
public class AfectadosController {
	
	@Autowired
    SiniestroService siniestroService;

	@Autowired
    PersonaService personaService;
	
	@Autowired
    DomicilioService domicilioService;
	
	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;	
	
	@Autowired
    TipoAfectacionService tipoAfectacionService;	

	@InitBinder
    protected void initBinder(WebDataBinder binder) {        
        binder.addValidators(new AfectadoValidator());
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String alta(@Valid Persona persona, BindingResult bindingResultP, 
			@Valid Domicilio domicilio, BindingResult bindingResultD, Model uiModel, HttpServletRequest request) {		
		List<FieldError> errores = new ArrayList<FieldError>();		
		// lista con todos los errores de validacion de afectados
		errores = UtilsValidacion.getErroresValidacion(bindingResultP.getFieldErrors());		
		errores.addAll(UtilsValidacion.getErroresValidacion(bindingResultD.getFieldErrors()));
		
		MensajeErrorValidacionJson mensajeError;
		JSONSerializer serializer = new JSONSerializer();
		
		if ( errores.size() == 0 ){
			if ( bindingResultP.getFieldErrors().size() > 0 ){
				// tiene errores que no son de validacion, mensaje de error general
				mensajeError =  new MensajeErrorValidacionJson( bindingResultP.getFieldErrors() );				
			} else if ( bindingResultD.getFieldErrors().size() > 0 ){
				mensajeError =  new MensajeErrorValidacionJson( bindingResultD.getFieldErrors() );
			} else {				
				if ( StringUtils.isEmpty(request.getParameter("sinId")) ){
					mensajeError = new MensajeErrorValidacionJson("No se han podido asociar los datos al siniestro");
				} else {
					TipoAfectacion ta = new TipoAfectacion();
					String tafId = request.getParameter("tafId");
					ta.setTafId(new Integer(tafId));
					if ( StringUtils.isNotEmpty( request.getParameter("adsId") ) ){
						// actualizar
						AfectadoDomicilioSiniestro ads = afectadoDomicilioSiniestroService.
								findAfectadoDomicilioSiniestro(Integer.parseInt(request.getParameter("adsId")));						
						// actualizar persona y domicilio
						personaService.updatePersona(persona);
						domicilioService.updateDomicilio(domicilio);	
						ads.setAdsTafId(ta);	
						afectadoDomicilioSiniestroService.updateAfectadoDomicilioSiniestro(ads);
						MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos se han actualizado con éxito", true);
						return serializer.exclude("class").serialize(mensajeExito);
					} else {				
						AfectadoDomicilioSiniestro ads = new AfectadoDomicilioSiniestro();
						Siniestro sin = new Siniestro();
						sin.setSinId( Integer.parseInt(request.getParameter("sinId")) );
						// grabar persona
 						personaService.savePersona(persona);
						// grabar domicilio
						domicilioService.saveDomicilio(domicilio);					
						ads.setAdsPerId(persona);
						ads.setAdsDomId(domicilio);
						ads.setAdsSinId(sin);
						ads.setAdsTafId(ta);					
						afectadoDomicilioSiniestroService.saveAfectadoDomicilioSiniestro(ads);
						MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos se han guardado con éxito", true);
						return serializer.exclude("class").serialize(mensajeExito);
					}
				}								
			}
		} else {
			// tiene errores de validacion
			mensajeError =  new MensajeErrorValidacionJson(errores);
		}
		return serializer.exclude("class").serialize(mensajeError); // class siempre lo lleva, hay que excluir 
    }	
	
	void populateEditForm(Model uiModel, Siniestro siniestro) {        
        uiModel.addAttribute("tiposAfectacion", tipoAfectacionService.findAllTipoAfectacions());        
    }
	
	// este para mostrar los afectados junto al propio siniestro
	@RequestMapping(value = "/{sinId}", produces = "text/html")
    public String show(@PathVariable("sinId") Integer sinId, Model uiModel) {
        uiModel.addAttribute("siniestro", siniestroService.findSiniestro(sinId));
        uiModel.addAttribute("itemId", sinId);
        return "siniestroes/show";
    }

	// actualizar datos PUT?
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siniestro);
            return "siniestroes/update";
        }
        uiModel.asMap().clear();
        siniestroService.updateSiniestro(siniestro);
        return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest);
    }

	// posible borrar todos los afectados
	@RequestMapping(value = "/remove/{adsId}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String delete(@PathVariable("adsId") Integer adsId, Model uiModel) {
		MensajeExitoJson mensajeExito = new MensajeExitoJson("Datos eliminados con éxito");
		JSONSerializer serializer = new JSONSerializer();
		AfectadoDomicilioSiniestro ads = afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(adsId);
        afectadoDomicilioSiniestroService.deleteAfectadoDomicilioSiniestro(ads);
        uiModel.asMap().clear();
        return serializer.exclude("class").serialize(mensajeExito);
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
