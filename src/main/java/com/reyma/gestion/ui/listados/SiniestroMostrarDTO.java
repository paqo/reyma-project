package com.reyma.gestion.ui.listados;

import java.io.Serializable;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;

public class SiniestroMostrarDTO implements Serializable {

	private static final long serialVersionUID = -2353462362547534441L;

	private Domicilio domicilio;
	private Persona afectado;
	private String tipo;
		
	public SiniestroMostrarDTO() {
		super();
	}
	
	public SiniestroMostrarDTO(Domicilio domicilio, Persona afectado, String tipo) {
		super();
		this.domicilio = domicilio;
		this.afectado = afectado;
		this.tipo = tipo;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public Persona getAfectado() {
		return afectado;
	}

	public void setAfectado(Persona afectado) {
		this.afectado = afectado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
