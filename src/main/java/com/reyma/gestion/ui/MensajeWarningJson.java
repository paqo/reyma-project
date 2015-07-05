package com.reyma.gestion.ui;

import java.io.Serializable;

public class MensajeWarningJson extends MensajeDialogoUIBase implements Serializable {

	private static final long serialVersionUID = 4646227887831123437L;
	
	public MensajeWarningJson() {
		super();
	}
	
	public MensajeWarningJson(String mensaje) {
		super(mensaje);
	}
	
	public MensajeWarningJson(String mensaje, String titulo) {
		super(mensaje, titulo);
	}
	
	
	
}
