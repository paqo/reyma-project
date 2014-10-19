package com.reyma.gestion.ui;

import java.io.Serializable;

public class MensajeExitoJson extends MensajeDialogoUIBase implements Serializable {
	
	private static final long serialVersionUID = 4006809887831023437L;
	
	public MensajeExitoJson() {
		super();
	}

	public MensajeExitoJson(String mensaje) {
		this.mensaje = mensaje;
		recargar = false;
		titulo = "Operaci&oacute;n realizada con &eacute;xito";
	}

	public MensajeExitoJson(String mensaje, boolean recargar) {
		this.mensaje = mensaje;
		this.recargar = recargar;	
		titulo = "Operaci&oacute;n realizada con &eacute;xito";
	}

}
