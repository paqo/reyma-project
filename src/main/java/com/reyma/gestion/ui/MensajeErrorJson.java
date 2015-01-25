package com.reyma.gestion.ui;

import java.io.Serializable;
import java.util.List;

public class MensajeErrorJson extends MensajeDialogoUIBase implements Serializable {

	private static final long serialVersionUID = 4646809887831123437L;
	
	public MensajeErrorJson() {
		super();
	}
	
	public MensajeErrorJson(List<String> errores) {
		mensaje = getMensajeError(errores);
		recargar = false;
		titulo = "Error";
	}	
	
	public MensajeErrorJson(String mensaje) {
		super(mensaje);
	}

	private String getMensajeError(List<String> errores) {		
		String aux = errores.size() > 1? "Se han producido los siguientes errores: <br/>" : "";		
		StringBuilder strBuilder = new StringBuilder(aux);				
		for (String error : errores) {
			strBuilder.append("&bull; " + error + "<br/>");
		}
		return strBuilder.toString();
	}	
}
