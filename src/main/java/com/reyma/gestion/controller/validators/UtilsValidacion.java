package com.reyma.gestion.controller.validators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.FieldError;

public class UtilsValidacion {

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
}
