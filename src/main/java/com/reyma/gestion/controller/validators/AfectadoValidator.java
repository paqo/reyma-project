package com.reyma.gestion.controller.validators;

import org.springframework.validation.Errors;
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
			UtilsValidacion.validarPersona(target, errors);				
		} else { // domicilio
			UtilsValidacion.validarDomicilio(target, errors);
		}	
			
	}
}