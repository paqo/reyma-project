package com.reyma.gestion.controller;
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

import com.reyma.gestion.controller.validators.AfectadoValidator;
import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoAfectacionService;
import com.reyma.gestion.ui.MensajeDialogoUIBase;
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
	
	@Autowired
	MunicipioService municipioService; 

	@InitBinder
    protected void initBinder(WebDataBinder binder) {        
        binder.addValidators(new AfectadoValidator());
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
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
						MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos se han actualizado con éxito", false);
						return serializer.exclude("class").serialize(mensajeExito);
					} else {				
						// crear nuevo
						AfectadoDomicilioSiniestro ads = new AfectadoDomicilioSiniestro();
						Siniestro sin = new Siniestro();
						sin.setSinId( Integer.parseInt(request.getParameter("sinId")) );
						
						Persona adsPersona = grabarPersona(persona);
						if ( adsPersona != null ){ // grabar persona							
							Domicilio adsDomicilio = grabarDomicilio(domicilio);
							if ( adsDomicilio != null ) { // grabar domicilio
								if ( grabarAfectadoDomicilioSiniestro(ads, adsPersona, adsDomicilio, sin, ta) ){
									MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos se han guardado con éxito", true);
									return serializer.exclude("class").serialize(mensajeExito);
								} else {
									mensajeError = new MensajeErrorValidacionJson("Revisar que todos los campos tengan valores correctos.");
									mensajeError.setTitulo("Error guardando los datos del formulario");
								}
							} else {
								mensajeError = new MensajeErrorValidacionJson("No se han podido guardar los datos del domicilio");
							}
						} else {
							mensajeError = new MensajeErrorValidacionJson("No se han podido guardar los datos del afectado");
						}						
					}
				}								
			}
		} else {
			// tiene errores de validacion
			mensajeError = new MensajeErrorValidacionJson(errores);
		}
		return serializer.exclude("class").serialize(mensajeError); // class siempre lo lleva, hay que excluir 
    }	
	
	private boolean grabarAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro ads, 
			Persona persona, Domicilio domicilio, Siniestro sin, TipoAfectacion ta) {
		ads.setAdsPerId(persona);
		ads.setAdsDomId(domicilio);
		ads.setAdsSinId(sin);
		ads.setAdsTafId(ta);
		try {
			afectadoDomicilioSiniestroService.saveAfectadoDomicilioSiniestro(ads);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping(value = "/remove/{adsId}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String delete(@PathVariable("adsId") Integer adsId, Model uiModel) {
		MensajeDialogoUIBase mensaje;
		JSONSerializer serializer = new JSONSerializer();
		AfectadoDomicilioSiniestro ads = afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(adsId);
        if ( afectadoDomicilioSiniestroService.deleteAfectadoDomicilioSiniestro(ads) ){
        	uiModel.asMap().clear();
        	mensaje = new MensajeExitoJson("Datos eliminados con éxito", true);
        } else {
        	mensaje = new MensajeErrorValidacionJson("Error eliminando datos del afectado");
        }
        return serializer.exclude("class").serialize(mensaje);
    }	
	
	private Persona grabarPersona(Persona persona){
		Persona personaEnc = null;		
		try {
			personaEnc = personaService.findPersona(persona);
			if ( personaEnc == null){
				// nueva persona, grabar
				personaService.savePersona(persona);
			} else {
				// persona encontrada, comprobar si han
				// cambiado los tlfs y en su caso grabar para no perder
				// esa info
				// TODO: los tlfs deberian estar en el siniestro!!				
				if ( !StringUtils.equals(persona.getPerTlf1(), personaEnc.getPerTlf1()) ||
					 !StringUtils.equals(persona.getPerTlf2(), personaEnc.getPerTlf2()) ){
					personaService.savePersona(persona);					
				} else {
					// persona ya almacenda, devolvemos la encontrada
					return personaEnc;				
				}
			}
			return persona;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Domicilio grabarDomicilio(Domicilio domicilio) {
		try {
			if ( domicilio.getDomMunId().getMunId() == null ){
				// municipio nuevo, crear primero
				Municipio nuevoMunicipio = new Municipio();
				nuevoMunicipio.setMunDescripcion(domicilio.getDomMunId().getMunDescripcion());
				nuevoMunicipio.setMunPrvId( domicilio.getDomProvId() );
				municipioService.saveMunicipio(nuevoMunicipio);
			}
			Domicilio domBusqueda = domicilioService.findDomicilio(domicilio);
			if ( domBusqueda == null ){
				domicilioService.saveDomicilio(domicilio);
				return domicilio;
			} else {
				// domicilio encontrado, devolvemos el persistido
				return domBusqueda;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
