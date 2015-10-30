package com.reyma.gestion.ui.customtags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.util.UtilsFactura;

public class ObtenerValorSubtotalRedondeoSuelo extends SimpleTagSupport {
	
	private LineaFactura linea;

	StringWriter sw = new StringWriter();

	public void doTag() throws JspException, IOException {
		if (linea != null) {			
			JspWriter out = getJspContext().getOut();
			out.println( UtilsFactura.obtenerSubtotal(linea) );			
		} else { // usar valor del body (no implementado)			
			getJspBody().invoke(sw);
			getJspContext().getOut().println(sw.toString());
		}
	}

	public void setLinea(LineaFactura linea) {
		this.linea = linea;
	}
	
}
