package com.reyma.gestion.ui;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.FieldError;

public class MensajeErrorValidacionJson extends MensajeDialogoUIBase implements Serializable {

	private static final long serialVersionUID = 4646809887831123437L;
	
	public MensajeErrorValidacionJson() {
		super();
	}
	
	public MensajeErrorValidacionJson(List<FieldError> errores) {
		mensaje = getMensajeError(errores);
		recargar = false;
		titulo = "Error(es) en los datos del formulario";
	}	
	
	public MensajeErrorValidacionJson(String mensaje) {
		super(mensaje);
	}

	private String getMensajeError(List<FieldError> errores) {		
		String aux = errores.size() > 1? "Se han producido los siguientes errores: <br/>" : "";		
		StringBuilder strBuilder = new StringBuilder(aux);				
		for (FieldError fieldError : errores) {
			if ( fieldError.isBindingFailure() ){
				strBuilder.append("&bull; El valor '" + fieldError.getRejectedValue() + "' para " +
						"el campo " + fieldError.getField()+ " no es correcto<br/>");
			} else {
				strBuilder.append("&bull; " + fieldError.getDefaultMessage() + "<br/>");
			}
		}
		return strBuilder.toString();
	}	
}
