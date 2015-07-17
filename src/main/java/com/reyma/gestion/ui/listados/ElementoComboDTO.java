package com.reyma.gestion.ui.listados;

import java.io.Serializable;

public class ElementoComboDTO implements Serializable {

	private static final long serialVersionUID = 7016323913168396523L;

	private Integer valor;
	private String label;
	
	public ElementoComboDTO() {
		super();
	}

	public ElementoComboDTO(Integer valor, String label) {
		super();
		this.valor = valor;
		this.label = label;
	}
	
	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}		
	
}
