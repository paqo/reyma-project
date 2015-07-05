package com.reyma.gestion.ui;

import java.io.Serializable;

public class LineaFacturaDTO implements Serializable {

	private static final long serialVersionUID = 2737804049614896725L;
	
	private Integer id;
	private String concepto;
	private Integer iva;
	private Double coste;
	
	public LineaFacturaDTO() {
		super();
	}
	
	public LineaFacturaDTO(Integer id, String concepto, Integer iva, Double coste) {
		super();
		this.id = id;
		this.concepto = concepto;
		this.iva = iva;
		this.coste = coste;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
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
				", coste: " + coste +
				"]";
	}
}
