package com.reyma.gestion.ui;

import java.io.Serializable;

public abstract class MensajeDialogoUIBase implements Serializable {

	private static final long serialVersionUID = 4646119887855123227L;
	protected String mensaje;
	protected String titulo;
	protected Boolean recargar;
	
	public MensajeDialogoUIBase() {
		super();
	}
	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Boolean getRecargar() {
		return recargar;
	}
	public void setRecargar(Boolean recargar) {
		this.recargar = recargar;
	}
	
	public MensajeDialogoUIBase(String mensaje, String titulo, Boolean recargar) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = recargar;	
	}
	
	public MensajeDialogoUIBase(String mensaje, String titulo) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = false;		
	}	
	
	public MensajeDialogoUIBase(String mensaje, Boolean recargar) {
		this.mensaje = mensaje;
		this.titulo = "";
		this.recargar = recargar;		
	}
	
	public MensajeDialogoUIBase(String mensaje) {
		this.mensaje = mensaje;
		this.recargar = false;
		this.titulo = "";
	}
}
