package com.reyma.gestion.ui;

import java.io.Serializable;

public class AutocompleteJSONBean implements Serializable {

	private static final long serialVersionUID = -1546347567658578585L;

	private String id;
	private String label;
	private String value;
	
	public AutocompleteJSONBean() {
		super();
	}
	
	public AutocompleteJSONBean(String id, String label, String value) {
		super();
		this.id = id;
		this.label = label;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
