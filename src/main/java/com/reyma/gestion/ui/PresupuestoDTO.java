package com.reyma.gestion.ui;

import java.io.Serializable;

public class PresupuestoDTO implements Serializable {
	
	private Integer idPresupuesto;
	private Integer idAfectado;
	private Integer idSiniestro;
	private String numPresupuesto;
	private String fechaPresupuesto;
	private LineaPresupuestoDTO[] lineasPresupuesto;

	private static final long serialVersionUID = 5863485364534653486L;
	
	public PresupuestoDTO() {
		super();
	}
	
	public Integer getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(Integer idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}

	public String getNumPresupuesto() {
		return numPresupuesto;
	}

	public void setNumPresupuesto(String numPresupuesto) {
		this.numPresupuesto = numPresupuesto;
	}

	public String getFechaPresupuesto() {
		return fechaPresupuesto;
	}

	public void setFechaPresupuesto(String fechaPresupuesto) {
		this.fechaPresupuesto = fechaPresupuesto;
	}

	public LineaPresupuestoDTO[] getLineasPresupuesto() {
		return lineasPresupuesto;
	}

	public void setLineasPresupuesto(LineaPresupuestoDTO[] lineasPresupuesto) {
		this.lineasPresupuesto = lineasPresupuesto;
	}

	public Integer getIdAfectado() {
		return idAfectado;
	}

	public void setIdAfectado(Integer idAfectado) {
		this.idAfectado = idAfectado;
	}

	public Integer getIdSiniestro() {
		return idSiniestro;
	}

	public void setIdSiniestro(Integer idSiniestro) {
		this.idSiniestro = idSiniestro;
	}
	
}
