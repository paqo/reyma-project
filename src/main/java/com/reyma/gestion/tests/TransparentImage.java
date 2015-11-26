package com.reyma.gestion.tests;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
 
 
public class TransparentImage {
 
    /** The resulting PDF file. */
    public static final String RESULT
        = "C:/temp/trans.pdf";
    /** One of the resources. */
    public static final String RESOURCE1
        = "C:/Users/Fcamarena/git/reyma-project/src/main/java/"
				+ "com/reyma/gestion/tests/logo.png";
    /** One of the resources. */
    public static final String RESOURCE2
        = "C:/Users/Fcamarena/git/reyma-project/src/main/webapp/images/logo.png"; 
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        Image img1 = Image.getInstance(RESOURCE1);
        // step 1
        Document document = new Document(img1);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        img1.setAbsolutePosition(0, 0);
        document.add(img1);         
        Image img4 = Image.getInstance(RESOURCE2);
        img4.setTransparency(new int[]{ 0xF0, 0xFF });
        img4.setAbsolutePosition(50, 50);
        document.add(img4);
 
        // step 5
        document.close();
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        new TransparentImage().createPdf(RESULT);
    }
}