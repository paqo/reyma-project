package com.reyma.gestion.busqueda.dto;

import java.io.Serializable;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;

public class ResultadoBusquedaCaducados extends ResultadoBusqueda implements Serializable {

	private static final long serialVersionUID = 6254833331653693510L;
	
	private int dias;
	
	public ResultadoBusquedaCaducados() {
		super();
	}
	
	public ResultadoBusquedaCaducados(Siniestro siniestro, Domicilio domicilio, Persona asegurado) {
		super(siniestro, domicilio, asegurado);		
	}
	
	public ResultadoBusquedaCaducados(ResultadoBusqueda resultado, int dias) {
		super(resultado);
		this.dias = dias;
	}
	
	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	@Override
	public String toString() {
		String padreToString = super.toString();
		return padreToString.substring(0, padreToString.length()-2) 
				+ ", dias: " + dias + "]";
	}
	
}
