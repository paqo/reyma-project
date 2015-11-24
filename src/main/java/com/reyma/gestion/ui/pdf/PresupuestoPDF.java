package com.reyma.gestion.ui.pdf;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;

public class PresupuestoPDF extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> modelo, Document document,
			PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Presupuesto pres = (Presupuesto) modelo.get("presupuesto");		
		List<LineaPresupuesto> lineas = pres.getLineasPresupuesto();
		// ordenar las lineas por id
		Collections.sort(lineas, new Comparator<LineaPresupuesto>() {
			@Override
			public int compare(LineaPresupuesto linea1, LineaPresupuesto linea2) {				
				return linea1.getLinId() < linea2.getLinId()? -1 :
						linea1.getLinId() > linea2.getLinId() ? 1 : 0;				
			}
		});
				
		Table table = new Table(3);
		table.addCell("Concepto");
		table.addCell("Importe");
		table.addCell("Iva");
		
		for (LineaPresupuesto linea : lineas) {
			table.addCell(linea.getLinConcepto());
			table.addCell(linea.getLinImporte().toString());
			table.addCell(linea.getLinIvaId().getIvaValor().toString());
		}
		
		document.add(table);

	}

}
