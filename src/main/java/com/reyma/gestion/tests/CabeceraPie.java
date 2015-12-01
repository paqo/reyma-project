package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class CabeceraPie extends PdfPageEventHelper {
		 
	protected Paragraph pie;
	protected PdfPTable cabecera;	
	private static Logger logger = Logger.getLogger( CabeceraPie.class );
	
	protected static StyleSheet styles = null;
	
	 /*public static final Font FUENTE_CABECERA = 
	    		FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD, new Color(255, 255, 255));*/
	 public static Font FUENTE_CABECERA;
	 public static final Font FUENTE_PIE = 
	    		FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(255, 255, 255));

	 static {	 
		 try {
			FUENTE_CABECERA  = 
				new Font(BaseFont.createFont("C:/Users/Fcamarena/git/reyma-project/src/main/java/" +
						"com/reyma/gestion/tests/arial.ttf", 
						BaseFont.WINANSI, BaseFont.EMBEDDED), 
						13, Font.BOLD, new Color(255, 255, 255));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	public CabeceraPie() {
		System.out.println("=> entra en constructor");
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
				
		cabecera = new PdfPTable(new float[]{15,85});
		cabecera.setWidthPercentage(100f);
		try { 
			
			//--RECOMENDADO: Spring way
			/*Resource resource = new ClassPathResource("com/reyma/gestion/tests/logo.png");
			Image img = Image.getInstance(resource.getURL());*/
			
			File logo = new File("C:/Users/Fcamarena/git/reyma-project/src/main/java/com/reyma/gestion/tests/logo.png");			
			Image img = Image.getInstance(logo.getPath());
			
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
	        
	        /*BaseFont bf= BaseFont.createFont("C:/Users/Fcamarena/git/reyma-project/src/main/java/com/reyma/gestion/tests/arial.afm", 
	        		BaseFont.WINANSI, BaseFont.EMBEDDED);
	        Font font = new Font(bf, 13, Font.BOLD, new Color(255, 255, 255));*/
	        
	        cell.addElement(new Phrase("REYMASUR", FUENTE_CABECERA));
	        //cell.addElement(new Phrase("REYMASUR", font));
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
		/*  DIMENSIONES PARA A4 sin margenes iniciales
     	anchura: 559
		márgenes: 36 (laterales y verticales)
		altura: 806.0
        */
		
		//Chunk c = new Chunk("REYMASUR13   •   reymasur@reymasur13.com   •   WWW.REYMASUR13.COM", FUENTE_PIE);
		Chunk c = new Chunk("REYMASUR13   |   reymasur@reymasur13.com   |   WWW.REYMASUR13.COM", FUENTE_PIE);
		c.setBackground(Color.decode("#69A322"), 78, 1.5f, 78, 3f);
		
		pie = new Paragraph(c);
		
		Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),
        		Element.ALIGN_CENTER, pie,
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 30, 0);
		
	}	
}