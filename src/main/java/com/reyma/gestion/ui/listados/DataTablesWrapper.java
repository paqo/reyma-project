package com.reyma.gestion.ui.listados;

import java.io.Serializable;
import java.util.List;

public class DataTablesWrapper implements Serializable {

	private static final long serialVersionUID = 1619350751764155067L;
	
	private List<SiniestroListadoDTO> aaData; 
	
	public DataTablesWrapper() {
		super();
	}

	public List<SiniestroListadoDTO> getAaData() {
		return aaData;
	}

	public void setAaData(List<SiniestroListadoDTO> aaData) {
		this.aaData = aaData;
	}	
	
}
