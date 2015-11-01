package com.reyma.gestion.busqueda.dto;

import java.io.Serializable;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.util.Fechas;

public class ResultadoBusqueda implements Serializable {

	private static final long serialVersionUID = -1254838046653693510L;
	
	private String id;
	private String compania;
	private String numeroSiniestro;
	private String fecha;
	private String domicilio;
	private String asegurado;
	private String estado;
	private String fechaOcurrencia;
	private String tipo;
	private String descripcion;
	

	public ResultadoBusqueda() {
		super();
	}
	
	public ResultadoBusqueda(Siniestro siniestro, Domicilio domicilio, Persona asegurado) {
		super();
		id = siniestro.getSinId().toString();
		compania = siniestro.getSinComId().getComCodigo();
		numeroSiniestro = siniestro.getSinNumero();
		fecha = Fechas.FORMATEADOR_DDMMYYYYHHMM.format(
				siniestro.getSinFechaEncargo().getTime());
		estado = siniestro.getSinEstId().getEstDescripcion();
		fechaOcurrencia = siniestro.getSinFechaOcurrencia() != null? Fechas.FORMATEADOR_DDMMYYYYHHMM.format(
				siniestro.getSinFechaOcurrencia().getTime()) : "";
		tipo = siniestro.getSinTsiId().getTsiDescripcion();
		descripcion = siniestro.getSinDescripcion();
		this.domicilio = domicilio != null? domicilio.getDomDireccion() : "";
		this.asegurado = asegurado != null? asegurado.getPerNombre() : "";		
	}

	// constructor copia
	public ResultadoBusqueda(ResultadoBusqueda resultado) {
		super();
		id = resultado.getId();
		compania = resultado.getCompania();
		numeroSiniestro = resultado.getNumeroSiniestro();
		fecha = resultado.getFecha();
		estado = resultado.getEstado();
		fechaOcurrencia = resultado.getFechaOcurrencia();
		tipo = resultado.getTipo();
		descripcion = resultado.getDescripcion();
		this.domicilio = resultado.getDomicilio();
		this.asegurado = resultado.getAsegurado();
	}

	public String getCompania() {
		return compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}

	public String getNumeroSiniestro() {
		return numeroSiniestro;
	}

	public void setNumeroSiniestro(String numeroSiniestro) {
		this.numeroSiniestro = numeroSiniestro;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getAsegurado() {
		return asegurado;
	}

	public void setAsegurado(String asegurado) {
		this.asegurado = asegurado;
	}
		
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFechaOcurrencia() {
		return fechaOcurrencia;
	}

	public void setFechaOcurrencia(String fechaOcurrencia) {
		this.fechaOcurrencia = fechaOcurrencia;
	}

	@Override
	public String toString() {
		return "[" + id + ", " + compania + ", " + fecha + ", "
				+ domicilio + ", " + asegurado + "]";
	}
	
}
