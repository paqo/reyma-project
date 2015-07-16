package com.reyma.gestion.ui.listados;

import java.io.Serializable;

public class SiniestroListadoDataTablesDTO implements Serializable {

	private static final long serialVersionUID = -1234908986577868450L;
	
	private Integer id;
	private String compania;
	private String numeroSiniestro;
	private String direccion;
	private String afectado;
	private String fecha;	
		
	public SiniestroListadoDataTablesDTO() {
		super();
	}
	
	public SiniestroListadoDataTablesDTO(SiniestroListadoDTO dto) {
		super();
		id = dto.getId();
		compania = dto.getCompania();
		numeroSiniestro = dto.getNumeroSiniestro();
		direccion = dto.getDireccion();
		afectado = dto.getAfectado();
		fecha = dto.getFecha().getTime().toString();		
	}
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroSiniestro() {
		return numeroSiniestro;
	}

	public void setNumeroSiniestro(String numeroSiniestro) {
		this.numeroSiniestro = numeroSiniestro;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getAfectado() {
		return afectado;
	}

	public void setAfectado(String afectado) {
		this.afectado = afectado;
	}

	public String getCompania() {
		return compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}	
	
}
