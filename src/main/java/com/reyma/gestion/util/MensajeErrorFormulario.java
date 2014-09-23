package com.reyma.gestion.util;

import java.io.Serializable;

import org.springframework.validation.FieldError;

public class MensajeErrorFormulario implements Serializable {

	private static final long serialVersionUID = 123456789L;

	private String campo;
	private String valor;
	
	public MensajeErrorFormulario() {
		super();
	}
	
	public MensajeErrorFormulario(FieldError fielError){
		super();
		campo = fielError.getField();
		valor = (String) fielError.getRejectedValue();		
	}
	
	@Override
	public String toString() {
		return "Error en el campo '" + campo + "': el valor " + valor + " no es correcto";
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
