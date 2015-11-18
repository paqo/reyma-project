package com.reyma.gestion.ui;

import java.io.Serializable;

public class LineaPresupuestoDTO implements Serializable {

	private static final long serialVersionUID = 2737804049614896725L;
	
	private Integer id;
	private Integer oficio;
	private String concepto;
	private Integer iva;
	private Double coste;
	
	public LineaPresupuestoDTO() {
		super();
	}
	
	public LineaPresupuestoDTO(Integer id, String concepto, Integer iva, Double coste) {
		super();
		this.id = id;
		this.concepto = concepto;
		this.iva = iva;		
		this.coste = coste;
	}
	
	public LineaPresupuestoDTO(Integer id, Integer oficio) {
		super();
		this.id = id;
		this.oficio = oficio;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	public Integer getOficio() {
		return oficio;
	}

	public void setOficio(Integer oficio) {
		this.oficio = oficio;
	}

	public Integer getIva() {
		return iva;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIva(Integer iva) {
		this.iva = iva;
	}

	public Double getCoste() {
		return coste;
	}

	public void setCoste(Double coste) {
		this.coste = coste;
	}
	
	@Override
	public String toString() {		
		return "[" +
				"id: " + id +				
				", concepto: " + concepto +
				", iva: " + iva +
				", oficio: " + oficio +
				", coste: " + coste +
				"]";
	}
}
