package com.reyma.gestion.ui.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public abstract  class CustomPresupuestoPDF extends AbstractView {

	public CustomPresupuestoPDF() {
		setContentType("application/pdf");
	}
	
	@Override
	protected boolean generatesDownloadContent() {		
		return true;
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		
	}
}
