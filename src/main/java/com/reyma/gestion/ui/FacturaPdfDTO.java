package com.reyma.gestion.ui;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.reyma.gestion.dao.LineaFactura;

public class FacturaPdfDTO implements Serializable {

	private static final long serialVersionUID = 5759238423348975509L;

	// datos factura
	private String numEncargo;
	private String numFactura;
	private String fechaEncargo;
	private String fechaFin;
	// datos perjudicado
	private String nombre;
	private String domicilio;
	private String cp;
	private String nif;	
	// lineas factura
	private Map<String, Set<LineaFactura>> lineasFactura;

	public FacturaPdfDTO() {
		super();
	}

	public String getNumEncargo() {
		return numEncargo;
	}

	public void setNumEncargo(String numEncargo) {
		this.numEncargo = numEncargo;
	}

	public String getNumFactura() {
		return numFactura;
	}

	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}

	public String getFechaEncargo() {
		return fechaEncargo;
	}

	public void setFechaEncargo(String fechaEncargo) {
		this.fechaEncargo = fechaEncargo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Map<String, Set<LineaFactura>> getLineasFactura() {
		return lineasFactura;
	}

	public void setLineasFactura(Map<String, Set<LineaFactura>> lineasFactura) {
		this.lineasFactura = lineasFactura;
	}	
	
}
