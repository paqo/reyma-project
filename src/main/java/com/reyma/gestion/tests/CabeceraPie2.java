package com.reyma.gestion.tests;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class CabeceraPie2 extends PdfPageEventHelper {
		 
	protected PdfPTable pie;
	private static final Font FUENTE_PIE = FontFactory.getFont(FontFactory.TIMES, 8, Font.NORMAL);
	private static final Chunk SALTO_LINEA = Chunk.NEWLINE;
	private String img_logo = "C:/Users/Fcamarena/git/reyma-project/src/main/webapp/images/logo.png";
	private String pie_linea_1 = "";
	private static Logger logger = Logger.getLogger( CabeceraPie2.class );

	public CabeceraPie2( float w ) {
		
		inicializaParametrosPie();
		
		float[] anchos = { 50f, 300f, 85f };
		pie = new PdfPTable( anchos );		
		pie.setTotalWidth( w );
		Image logo = null;
		
		try {
			logo = Image.getInstance(img_logo);
		} catch (BadElementException e) {
			logger.error(e);
		} catch (MalformedURLException e) {
			logger.error("ruta del logo de cabecera mal formada: " + img_logo + "\n", e);
		} catch (IOException e) {
			logger.error("error I/O:\n",e);
		}
		PdfPCell celda = new PdfPCell();
		celda.setBorderWidth(0f);
		pie.addCell(celda);
		Paragraph p = new Paragraph();
		p.setFont( FUENTE_PIE ); 
		p.add( new Phrase(pie_linea_1));
		p.add(SALTO_LINEA);		
		celda = new PdfPCell( p );
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		celda.setBorderWidth(0f);
		pie.addCell(celda);
		if ( logo != null ){
			celda = new PdfPCell( logo );
			celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
			celda.setBorderWidth(0f);
			pie.addCell( celda );
		}else {
			logger.warn("AVISO: no se ha podido incluir la imagen del logo AENOR en el pie");
		}
					
	}
	
	private void inicializaParametrosPie(){		
		img_logo = "C:/Users/Fcamarena/git/reyma-project/src/main/java/com/reyma/gestion/tests/logo.png";
		pie_linea_1 = "este es el pie";
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		try {
			document.add(new Paragraph("Prueba"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		pie.writeSelectedRows(0, -1,
				(document.right() - document.left() - 400) / 2, document.bottom()-15, cb);
	}
}
