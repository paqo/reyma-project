package com.reyma.gestion.ui.listados;

import java.io.Serializable;

public class PresupuestoListadoDTO implements Serializable {

	private static final long serialVersionUID = 52010687300504820L;

	private Integer id;
	private Integer idAfectado;
	private String numPresupuesto;
	private String fechaPresupuesto;
	
	public PresupuestoListadoDTO() {
		super();
	}
	
	public PresupuestoListadoDTO(Integer id, Integer idAfectado, String numPresupuesto, String fechaPresupuesto) {
		super();
		this.id = id;
		this.idAfectado = idAfectado;
		this.numPresupuesto = numPresupuesto;
		this.fechaPresupuesto = fechaPresupuesto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getIdAfectado() {
		return idAfectado;
	}

	public void setIdAfectado(Integer idAfectado) {
		this.idAfectado = idAfectado;
	}

	public String toString() {		
		return "[" +
				"id: " + id + 
				"numPresupuesto: " + numPresupuesto + 
				"idAfectado: " + idAfectado +
				"fechaPresupuesto: " + fechaPresupuesto +
				"]";
	}
		
}
