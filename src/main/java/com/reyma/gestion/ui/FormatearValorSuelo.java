package com.reyma.gestion.ui;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FormatearValorSuelo extends SimpleTagSupport {

	private String valor;

	StringWriter sw = new StringWriter();

	public void doTag() throws JspException, IOException {
		if (valor != null) {			
			JspWriter out = getJspContext().getOut();
			BigDecimal res = new BigDecimal(valor);
			out.println( res.setScale(2, RoundingMode.FLOOR) );
		} else { // usaria del body (no se usa)			
			getJspBody().invoke(sw);
			getJspContext().getOut().println(sw.toString());
		}
	}

	public void setValor(String valor) {
		this.valor = valor;
	}	
	
}
