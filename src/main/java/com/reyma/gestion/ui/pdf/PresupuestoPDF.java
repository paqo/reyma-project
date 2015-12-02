package com.reyma.gestion.ui.pdf;

import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Presupuesto;

public class PresupuestoPDF extends AbstractPdfView {
	
	private CabeceraPie cabeceraPie;
	
	@Override
	protected void prepareWriter(Map<String, Object> model, PdfWriter writer,
			HttpServletRequest request) throws DocumentException {
		
		cabeceraPie = new CabeceraPie();
		
		writer.setBoxSize("art", new Rectangle(36, 40, 559, 788));
		writer.setPageEvent( cabeceraPie );		
	}
	
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
		
        // step 1
        //document = new Document(PageSize.A4);              
        // step 3
        document.open();
        // step 4
        PdfPTable tabla = getTablaDatosAfectado();
        tabla.setSpacingBefore(15);
        tabla.setSpacingAfter(10);   
        document.add(tabla);                
        // step 5
        PdfPTable tablaLineas = getTable(lineas);
        document.add(tablaLineas);        
        // step 6
        cabeceraPie.setContentLeft(false);
        document.close();
        System.out.println("terminado!");
		
	}
	
}
