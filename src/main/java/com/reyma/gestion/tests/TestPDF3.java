package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
 
public class TestPDF3 {
    /** Path to the resulting PDF */
    public static final String RESULT = "C:/temp/res.pdf";
 
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new TestPDF3().createPdf(RESULT);
        System.out.println("=> terminado");
        System.exit(0);
    }
 
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer =
            PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setStrictImageSequence(true);
        //writer.setInitialLeading(18);
        // step 3
        document.open();
        // step 4
        // Create an image
        Image img = Image.getInstance("C:/Users/Fcamarena/git/reyma-project/src/main/java/com/reyma/gestion/tests/logo.png");
        img.setAlignment(Image.LEFT | Image.TEXTWRAP);
        img.setBorder(Image.BOX);
        img.setBorderWidth(10);
        img.setBorderColor(Color.WHITE);
        //img.scaleToFit(1000, 72);
        img.setAbsolutePosition(document.leftMargin(), writer.getVerticalPosition(false));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.rectangle(document.leftMargin(), writer.getVerticalPosition(false), document.getPageSize().getWidth(), 40); 
        canvas.setColorFill(Color.GREEN);
        canvas.fill();
        canvas.addImage(img);
                
        //document.add(img);
        
        // Create text elements 
        document.add(new Paragraph("Titulo", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD) ));       
        document.add(new Paragraph("2015"));
        document.add(new Paragraph(
            String.format("Duration: %d minutes", 120)));
        document.add(new Paragraph("Directors:"));
        document.add(new Paragraph("Lista de directores"));
        document.add(Chunk.NEWLINE);
        // step 5
        document.close();
    }
}