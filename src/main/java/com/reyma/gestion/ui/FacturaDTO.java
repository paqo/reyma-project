package com.reyma.gestion.ui;

import java.io.Serializable;

public class FacturaDTO implements Serializable {
	
	private Integer idFactura;
	private Integer idAfectado;
	private String numFactura;
	private String fechaFactura;
	private LineaFacturaDTO[] lineasFactura;

	private static final long serialVersionUID = 5863485364534653486L;
	
	public FacturaDTO() {
		super();
	}

	public Integer getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
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

	public LineaFacturaDTO[] getLineasFactura() {
		return lineasFactura;
	}

	public void setLineasFactura(LineaFacturaDTO[] lineasFactura) {
		this.lineasFactura = lineasFactura;
	}

	public Integer getIdAfectado() {
		return idAfectado;
	}

	public void setIdAfectado(Integer idAfectado) {
		this.idAfectado = idAfectado;
	}
	
}
