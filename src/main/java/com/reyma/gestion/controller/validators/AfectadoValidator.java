package com.reyma.gestion.controller.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;

public class AfectadoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Persona.class.isAssignableFrom(clazz) ||
			   Domicilio.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {	
		if ( Persona.class.isInstance(target) ){
			Persona persona = (Persona) target;
			// al menos un telefono,nombre no vacío y nif valido
			if ( StringUtils.isBlank(persona.getPerTlf1()) && 
					StringUtils.isBlank(persona.getPerTlf2())){
				errors.rejectValue("perTlf1", "field.min.length",
		                null,"debe rellenar al menos un teléfono de contacto");
			}
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "perNombre", "field.min.length",
	                null,"el nombre del afectado no puede ser vacío");
			if ( StringUtils.isNotBlank(persona.getPerNif()) ){
				// si hay NIF, validar				
				if (!UtilsValidacion.esNIFValido(persona.getPerNif())){
					errors.rejectValue("perNif", "field.min.length",
			                null,"el NIF no es válido");
				}
			}
				
		} else { // domicilio
			Domicilio domicilio = (Domicilio) target;
			if ( domicilio.getDomDireccion() == null || domicilio.getDomDireccion().trim().isEmpty() ){
				errors.rejectValue("domDireccion", "field.min.length",
		                null,"el domicilio no puede ser vacío");
			}
		}	
			
	}	
}