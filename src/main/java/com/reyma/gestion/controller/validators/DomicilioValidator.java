package com.reyma.gestion.controller.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.reyma.gestion.dao.Domicilio;

public class DomicilioValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Domicilio.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Domicilio domicilio = (Domicilio) target;
		if ( domicilio.getDomDireccion() == null || domicilio.getDomDireccion().trim().isEmpty() ){
			errors.rejectValue("domDireccion", "field.min.length",
	                null,"la dirección no puede ser vacía");
		}		
	}
}