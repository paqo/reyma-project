package com.reyma.gestion.controller.validators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.aeat.valida.Validador;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;

public class UtilsValidacion {

	public static Validador validadorNIF = new Validador();
	private static ResourceBundleMessageSource mensajes;	
	private static Locale local = new Locale("es", "ES");
	static {
		mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("mensajes-validacion");
	}
	
	public static List<FieldError> getErroresValidacion( List<FieldError> errores) {
		FieldError fieldError;
		List<FieldError> res = new ArrayList<FieldError>();
		// filtrar errores que no son de validacion
		Iterator<FieldError> it = errores.iterator();
		while (it.hasNext()) {
			fieldError = (FieldError) it.next();
			if ( !fieldError.isBindingFailure() ){
				res.add(fieldError);
			}
		}	
		return res;
	}
	
	public static void validarDomicilio(Object target, Errors errors) {
		Domicilio domicilio = (Domicilio) target;
		// al menos direccion (prov y mun se seleccionan de desplegable) 
		// y CP valido 
		if ( StringUtils.isBlank(domicilio.getDomDireccion()) ){
			errors.rejectValue("domDireccion", "", null, obtenerMensaje("domicilio.direccion"));
		}
		if ( domicilio.getDomCp() != null && domicilio.getDomCp() < 10000 ){
			errors.rejectValue("domCp", "", null, obtenerMensaje("domicilio.cp"));
		}
	}

	public static void validarPersona(Object target, Errors errors) {
		Persona persona = (Persona) target;
		// al menos un telefono,nombre no vacío y nif valido
		if ( StringUtils.isBlank(persona.getPerTlf1()) && 
				StringUtils.isBlank(persona.getPerTlf2())){
			errors.rejectValue("perTlf1", "", null, obtenerMensaje("persona.tlf"));
		}
		if ( StringUtils.isBlank(persona.getPerNombre()) ){			
			errors.rejectValue("perNombre", "", null, obtenerMensaje("persona.nombre"));
		}		
		if ( StringUtils.isNotBlank(persona.getPerNif()) ){
			// si hay NIF, validar				
			if (!UtilsValidacion.esNIFValido(persona.getPerNif())){
				errors.rejectValue("perNif", "", null, obtenerMensaje("persona.nif"));
			}
		}
	}
	
	public static boolean esNIFValido(String nif) {		
		return validadorNIF.checkNif(nif) == Validador.NIF_OK;
	}
	
	public static String obtenerMensaje(String codigo, Object[] argumentos){
		try {
			return mensajes.getMessage(codigo, argumentos, local);
		} catch (NoSuchMessageException e) {
			e.printStackTrace();
			return "(mensaje no disponible)";			
		}
	}
	
	public static String obtenerMensaje(String codigo){
		return obtenerMensaje(codigo, null);
	}
}
