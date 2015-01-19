package com.reyma.gestion.integracion;

import java.io.Serializable;

public class PerjudicadoDTO implements Serializable {

	private static final long serialVersionUID = -4523627247675672367L;
	
	private String perjNombre;
	private String perjDireccion;
	private String perjTlf1;
	private String perjTlf2;
	private String perjProvId;
	private Integer perjCp;
	private String perjMunId;
	
	public PerjudicadoDTO() {
		super();
	}

	public String getPerjNombre() {
		return perjNombre;
	}

	public void setPerjNombre(String perjNombre) {
		this.perjNombre = perjNombre;
	}

	public String getPerjDireccion() {
		return perjDireccion;
	}

	public void setPerjDireccion(String perjDireccion) {
		this.perjDireccion = perjDireccion;
	}

	public String getPerjTlf1() {
		return perjTlf1;
	}

	public void setPerjTlf1(String perjTlf1) {
		this.perjTlf1 = perjTlf1;
	}

	public String getPerjTlf2() {
		return perjTlf2;
	}

	public void setPerjTlf2(String perjTlf2) {
		this.perjTlf2 = perjTlf2;
	}

	public String getPerjProvId() {
		return perjProvId;
	}

	public void setPerjProvId(String perjProvId) {
		this.perjProvId = perjProvId;
	}

	public Integer getPerjCp() {
		return perjCp;
	}

	public void setPerjCp(Integer perjCp) {
		this.perjCp = perjCp;
	}

	public String getPerjMunId() {
		return perjMunId;
	}

	public void setPerjMunId(String perjMunId) {
		this.perjMunId = perjMunId;
	}

}
