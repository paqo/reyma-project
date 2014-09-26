package com.reyma.gestion.controller.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.reyma.gestion.dao.Persona;

public class PersonaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Persona.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Persona persona = (Persona) target;
		if ( persona.getPerNif() == null || persona.getPerNif().trim().isEmpty() ){
			errors.rejectValue("perNif", "field.min.length",
	                null,"el nif no puede ser vacío");
		}		
	}
}