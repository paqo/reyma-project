package com.reyma.gestion.ui.listados;

import java.io.Serializable;

public class FacturaListadoDTO implements Serializable {

	private static final long serialVersionUID = 52010687300504820L;

	private Integer id;
	private String numFactura;
	private String fechaFactura;
	
	public FacturaListadoDTO() {
		super();
	}
	
	public FacturaListadoDTO(Integer id, String numFcatura, String fechaFactura) {
		super();
		this.id = id;
		this.numFactura = numFcatura;
		this.fechaFactura = fechaFactura;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumFactura() {
		return numFactura;
	}

	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}

	public String getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	
	public String toString() {		
		return "[" +
				"id: " + id + 
				"numFactura: " + numFactura + 
				"fechaFactura: " + fechaFactura +
				"]";
	}
		
}
