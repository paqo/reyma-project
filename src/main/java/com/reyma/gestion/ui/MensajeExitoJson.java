package com.reyma.gestion.ui;

import java.io.Serializable;

public class MensajeExitoJson implements Serializable {

	//TODO: Hacer clase padre para mensaje general de la que hereden todos
	
	private static final long serialVersionUID = 4646809887831123437L;
	private String mensaje;
	private String titulo;
	private Boolean recargar;
	
	public MensajeExitoJson() {
		super();
	}

	public MensajeExitoJson(String mensaje) {
		this.mensaje = mensaje;
		this.recargar = false;
		this.titulo = "Operaci&oacute;n realizada con &eacute;xito";
	}

	public MensajeExitoJson(String mensaje, String titulo, Boolean recargar) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = recargar;	
	}
	
	public MensajeExitoJson(String mensaje, String titulo) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = false;		
	}	
	
	public MensajeExitoJson(String mensaje, Boolean recargar) {
		this.mensaje = mensaje;
		this.titulo = "";
		this.recargar = recargar;		
	}	
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Boolean getRecargar() {
		return recargar;
	}

	public void setRecargar(Boolean recargar) {
		this.recargar = recargar;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}	

}
