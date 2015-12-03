package com.reyma.gestion.ui.pdf;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.util.Fechas;

public class CabeceraPie extends PdfPageEventHelper {

	protected Paragraph pie;
	protected PdfPTable cabecera;	
	private static Logger logger = Logger.getLogger( CabeceraPie.class );
	private boolean contentLeft;
	private Calendar fecha;

	public static Font FUENTE_CABECERA;
	public static final Font FUENTE_PIE = 
			FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(255, 255, 255));

	static {	 
		try {
			Resource resource = new ClassPathResource("com/reyma/gestion/tests/arial.ttf");
			FUENTE_CABECERA  = 
					/*new Font(BaseFont.createFont("C:/Users/Fcamarena/git/reyma-project/src/main/java/" +
							"com/reyma/gestion/tests/arial.ttf", 
							BaseFont.WINANSI, BaseFont.EMBEDDED), 
							13, Font.BOLD, new Color(255, 255, 255));*/
					new Font(BaseFont.createFont(resource.getFile().getAbsolutePath(), 
							BaseFont.WINANSI, BaseFont.EMBEDDED), 
							13, Font.BOLD, new Color(255, 255, 255));
		} catch (DocumentException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	public CabeceraPie() {
		contentLeft = true;		
	}
	
	public CabeceraPie(Calendar fecha) {
		contentLeft = true;		
		this.fecha = fecha;
	}
	
	

	public boolean isContentLeft() {
		return contentLeft;
	}

	public void setContentLeft(boolean contentLeft) {
		this.contentLeft = contentLeft;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {

		cabecera = new PdfPTable(new float[]{15,85});
		cabecera.setWidthPercentage(100f);
		try { 

			//-- Spring way:
			Resource resource = new ClassPathResource("com/reyma/gestion/tests/logo.png");
			Image img = Image.getInstance(resource.getURL());

			// --local
			/*File logo = new File("C:/Users/Fcamarena/git/reyma-project/src/main/java/com/reyma/gestion/tests/logo.png");			
			Image img = Image.getInstance(logo.getPath());*/

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
		/*  
		DIMENSIONES PARA A4 sin margenes iniciales
     	anchura: 559
		márgenes: 36 (laterales y verticales)
		altura: 806.0
		*/
		if (contentLeft){
			getPie(writer);
		} else {
			getPieUltimaPagina(writer, document);
		}
		
	}	

	/*
	 * pie simplificado para paginas del medio
	 */
	private void getPie(PdfWriter writer) {
		// caracter de separacion: •
		Chunk c = new Chunk("REYMASUR13   |   reymasur@reymasur13.com   |   WWW.REYMASUR13.COM", FUENTE_PIE);

		c.setBackground(Color.decode("#69A322"), 78, 1.5f, 78, 3f);

		pie = new Paragraph(c);

		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER, pie,
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 30, 0);
	}

	/*
	 *  pie con todos los datos
	 */
	private void getPieUltimaPagina(PdfWriter writer, Document document) {
		
    	try {
    		
    		// 1.- datos de la empresa y fecha
    		
    		PdfPTable tabDatosEmp = new PdfPTable(new float[] { 1, 1});
    		
    		tabDatosEmp.setWidthPercentage(100f);
    		tabDatosEmp.getDefaultCell().setPadding(5);
    		tabDatosEmp.getDefaultCell().setBackgroundColor(Color.decode("#EDEFEA"));
    		
    		Paragraph datosFecha = new Paragraph();
    		datosFecha.add(Chunk.NEWLINE); 
    		datosFecha.add(Chunk.NEWLINE); 
    		datosFecha.add(Chunk.NEWLINE); 
    		datosFecha.add(Chunk.NEWLINE); 
    		datosFecha.add(Chunk.NEWLINE);
    		datosFecha.add(Chunk.NEWLINE);
    		datosFecha.add(Chunk.NEWLINE);
    		datosFecha.add(new Chunk("FECHA: " + Fechas.formatearFechaDDMMYYYY( fecha.getTime() ) ));
    		
        	tabDatosEmp.getDefaultCell().setBorder(Rectangle.NO_BORDER);		
        	tabDatosEmp.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        	tabDatosEmp.addCell(datosFecha);

        	Paragraph datosEmp = new Paragraph();
        	datosEmp.add(new Chunk("REYMASUR 13, S.L.U"));
        	datosEmp.add(Chunk.NEWLINE); 
        	datosEmp.add(new Chunk("Avenida de Averroes 6, Planta 2, Módulo 3")); 
        	datosEmp.add(Chunk.NEWLINE);  
        	datosEmp.add(new Chunk("CP: 41020, Sevilla (SEVILLA)"));
        	datosEmp.add(Chunk.NEWLINE);  
        	datosEmp.add(new Chunk("CIF: B90118837"));
        	datosEmp.add(Chunk.NEWLINE); 
        	datosEmp.add(new Chunk("TLF: 954932515, FAX: 954517111"));
        	tabDatosEmp.addCell(datosEmp);
        	//tabDatosEmp.setSpacingBefore(30);
			//document.add(tabDatosEmp);
        				       	
        	//-- IMPORTANTE: necesario que tenga definida la
        	// anchura total de manera absoluta o no funciona!
        	if(tabDatosEmp.getTotalWidth()==0)
        		tabDatosEmp.setTotalWidth((document.right()-document.left())*tabDatosEmp.getWidthPercentage()/100f);        	
        	
        	//System.out.println("=> " + tabDatosEmp.getTotalHeight());
        	tabDatosEmp.writeSelectedRows(0, -1, document.left(), document.bottom() + tabDatosEmp.getTotalHeight(), writer.getDirectContent());
        	
			//2.- añadir tambien pie simplificado
			getPie(writer);			
			
		} catch (/*Document*/Exception e) {
			logger.error(e);
		}

	}
}