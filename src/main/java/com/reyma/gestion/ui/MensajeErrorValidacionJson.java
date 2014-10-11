package com.reyma.gestion.ui;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.FieldError;

public class MensajeErrorValidacionJson implements Serializable {

	//TODO: Hacer clase padre para mensaje general de la que hereden todos
	
	private static final long serialVersionUID = 4646809887831123437L;
	private String mensaje;
	private String titulo;
	private Boolean recargar;
	
	public MensajeErrorValidacionJson() {
		super();
	}

	public MensajeErrorValidacionJson(String mensaje) {
		this.mensaje = mensaje;
		this.recargar = false;
		this.titulo = "";
	}
	
	public MensajeErrorValidacionJson(List<FieldError> errores) {
		this.mensaje = getMensajeError(errores);
		this.recargar = false;
		this.titulo = "Error(es) en los datos del formulario";
	}

	public MensajeErrorValidacionJson(String mensaje, String titulo, Boolean recargar) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = recargar;	
	}
	
	public MensajeErrorValidacionJson(String mensaje, String titulo) {
		this.mensaje = mensaje;
		this.titulo = titulo;
		this.recargar = false;		
	}	
	
	public MensajeErrorValidacionJson(String mensaje, Boolean recargar) {
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
