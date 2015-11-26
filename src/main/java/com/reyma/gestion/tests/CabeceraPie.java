package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class CabeceraPie extends PdfPageEventHelper {
		 
	protected PdfPTable pie;
	protected PdfPTable cabecera;	
	private String img_logo;	
	private static Logger logger = Logger.getLogger( CabeceraPie.class );
	
	 public static final Font FUENTE_CABECERA = 
	    		FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
	 public static final Font FUENTE_PIE = 
	    		FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255));

	public CabeceraPie() {
		// parametros
		inicializaParametros();
	}
	
	private void inicializaParametros(){		
		img_logo = "C:/Users/Fcamarena/git/reyma-project/src/main/webapp/images/logo.png";
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		cabecera = new PdfPTable(new float[]{15,85});
		cabecera.setWidthPercentage(100f);
		try {
			// imagen
			Image img = Image.getInstance(img_logo);
			PdfPCell cell = new PdfPCell(img);
			cell.setBackgroundColor(Color.decode("#69A322"));
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setPaddingLeft(10f);
	        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER | Rectangle.ALIGN_MIDDLE);
	        cabecera.addCell(cell);
	        // nombre
	        cell = new PdfPCell();
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setBackgroundColor(Color.decode("#69A322"));
	        cell.setPaddingLeft(5f);
	        cell.setPaddingTop(10f);
	        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT | Rectangle.ALIGN_MIDDLE);
	        cell.addElement(new Phrase("TEST", FUENTE_CABECERA));
	        cabecera.addCell(cell);
			document.add(cabecera);
		} catch (DocumentException e) {
			logger.error(e);
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
		
	public void onEndPage(PdfWriter writer, Document document) {
		pie = new PdfPTable(new float[]{30,40,30});
		pie.setWidthPercentage(100f);
		try {
	        PdfPCell cell = new PdfPCell();
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setBackgroundColor(Color.decode("#69A322"));	        
	        cell.setHorizontalAlignment(Rectangle.ALIGN_RIGHT | Rectangle.ALIGN_MIDDLE);
	        cell.addElement(new Phrase("REYMASUR13", FUENTE_PIE));
	        pie.addCell(cell);
	        cell = new PdfPCell();
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setBackgroundColor(Color.decode("#69A322"));	        
	        cell.setHorizontalAlignment(Rectangle.ALIGN_CENTER | Rectangle.ALIGN_MIDDLE);
	        cell.addElement(new Phrase("• reymasur@reymasur13.com •", FUENTE_PIE));
	        pie.addCell(cell);
	        cell = new PdfPCell();
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setBackgroundColor(Color.decode("#69A322"));	        
	        cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT | Rectangle.ALIGN_MIDDLE);
	        cell.addElement(new Phrase("WWW.REYMASUR13.COM", FUENTE_PIE));
	        pie.addCell(cell);
			document.add(pie);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
