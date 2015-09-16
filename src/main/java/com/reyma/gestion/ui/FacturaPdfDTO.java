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
	private String fechaFactura;
	// datos perjudicado
	private String nombre;
	private String domicilio;
	private String cp;
	private String nif;	
	// lineas factura
	private Map<String, Set<LineaFactura>> lineasFactura;
	// datos reymasur
	private String nombreR;
	private String domicilioR;
	private String localidadR;
	private String cpR;
	private String nifR;
	private String telefonoR;
	private String faxR;	
	private String urlR;
	private String nombreCortoR;
	private String emailR;
	

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
	
	public String getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
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

	public String getNombreR() {
		return nombreR;
	}

	public void setNombreR(String nombreR) {
		this.nombreR = nombreR;
	}

	public String getDomicilioR() {
		return domicilioR;
	}

	public void setDomicilioR(String domicilioR) {
		this.domicilioR = domicilioR;
	}

	public String getLocalidadR() {
		return localidadR;
	}

	public void setLocalidadR(String localidadR) {
		this.localidadR = localidadR;
	}

	public String getCpR() {
		return cpR;
	}

	public void setCpR(String cpR) {
		this.cpR = cpR;
	}

	public String getNifR() {
		return nifR;
	}

	public void setNifR(String nifR) {
		this.nifR = nifR;
	}

	public String getUrlR() {
		return urlR;
	}

	public void setUrlR(String urlR) {
		this.urlR = urlR;
	}

	public String getNombreCortoR() {
		return nombreCortoR;
	}

	public void setNombreCortoR(String nombreCortoR) {
		this.nombreCortoR = nombreCortoR;
	}

	public String getEmailR() {
		return emailR;
	}

	public void setEmailR(String emailR) {
		this.emailR = emailR;
	}

	public String getTelefonoR() {
		return telefonoR;
	}

	public void setTelefonoR(String telefonoR) {
		this.telefonoR = telefonoR;
	}

	public String getFaxR() {
		return faxR;
	}

	public void setFaxR(String faxR) {
		this.faxR = faxR;
	}	
	
	
	
}
