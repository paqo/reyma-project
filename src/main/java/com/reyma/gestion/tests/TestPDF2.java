package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.dao.LineaPresupuesto;
  
public class TestPDF2 {
 
	/** The resulting PDF file. */
    public static final String RESULT = "c:/temp/test_c.pdf";
    
    public static final Font FUENTE_CABECERA_COLS = 
    		FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException     
     */
    public void createPdf(String filename) throws DocumentException, IOException {
         
        // step 1
        Document document = new Document(PageSize.A4);              
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setBoxSize("art", new Rectangle(36, 40, 559, 788));
        writer.setPageEvent( new CabeceraPie() );       
        // step 3
        document.open();
        // step 4
        PdfPTable table = getTable(getLineas());
        document.add(table);        
        // step 5
        document.close();
        System.out.println("terminado!");
        System.exit(0);
    }
    
    private List<LineaPresupuesto> getLineas() {	
    	
    	List<LineaPresupuesto> listaTest = new ArrayList<LineaPresupuesto>();
    	for ( int i=0; i<50; i++ ){
    		listaTest.add(getLineaRandom());
    	}    	
    	return listaTest;
	}



	private LineaPresupuesto getLineaRandom() {
		Iva iva = new Iva();
		LineaPresupuesto res = new LineaPresupuesto();
		res.setLinConcepto("Concepto" + RandomStringUtils.randomNumeric(1));
		res.setLinImporte(new BigDecimal(RandomStringUtils.randomNumeric(4)));
		long aux = System.currentTimeMillis();
		if ( aux %2 == 0 ){			
			iva.setIvaValor(21);
		}else {
			iva.setIvaValor(10);
		}
		res.setLinIvaId(iva);
		return res;
	}
	
	/**
     * Creates a table with film festival screenings.
     * @param connection a database connection
     * @param day a film festival day
     * @return a table with screenings.
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    public PdfPTable getTable(List<LineaPresupuesto> lineas)
        throws DocumentException, IOException {
        PdfPTable tabLineas = new PdfPTable(new float[] { 5, // descripcion
        											  2, // base imp
        											  1, // porcentaje IVA
        											  2, // cant. iva
        											  3  // subtotal 
        											 });        
        tabLineas.setWidthPercentage(100f);
        // cabecera columnas        
        tabLineas.getDefaultCell().setPadding(5); 
        tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tabLineas.getDefaultCell().setColspan(1);        
        tabLineas.getDefaultCell().setBorderColor(Color.BLACK);        
        tabLineas.getDefaultCell().setBorderWidth(0.5f);
        tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#20A627"));
        tabLineas.addCell(new Phrase("Descripcion", FUENTE_CABECERA_COLS));        
        tabLineas.addCell(new Phrase("Base", FUENTE_CABECERA_COLS));
        tabLineas.addCell(new Phrase("%", FUENTE_CABECERA_COLS));
        tabLineas.addCell(new Phrase("IVA", FUENTE_CABECERA_COLS));
        tabLineas.addCell(new Phrase("Subtotal", FUENTE_CABECERA_COLS));
        // oficio 1
        tabLineas.getDefaultCell().setBorderColor(Color.BLACK);
        tabLineas.getDefaultCell().setPadding(5); 
        tabLineas.getDefaultCell().setUseAscender(true);
        tabLineas.getDefaultCell().setUseDescender(true);
        tabLineas.getDefaultCell().setColspan(5);
        tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#A6EDA9")); 
        tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabLineas.addCell("ALBAÑILERIA");
        tabLineas.getDefaultCell().setBackgroundColor(null);  
        tabLineas.getDefaultCell().setColspan(1);
        // 3 lineas especiales
        tabLineas.setHeaderRows(1);
        // de las cuales, una el pie
        //table.setFooterRows(1);
        // lineas
        tabLineas.getDefaultCell().setBorderColor(Color.BLACK);
        for (LineaPresupuesto linea : lineas) {
        	tabLineas.getDefaultCell().setBorder(Rectangle.LEFT);
        	tabLineas.addCell(linea.getLinConcepto());
        	tabLineas.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tabLineas.addCell(linea.getLinImporte().toString());
            tabLineas.addCell(String.valueOf(linea.getLinIvaId().getIvaValor()));
            tabLineas.addCell(RandomStringUtils.randomNumeric(2));
            tabLineas.getDefaultCell().setBorder(Rectangle.RIGHT);
            tabLineas.addCell(RandomStringUtils.randomNumeric(4));
		}    
        // total
        tabLineas.getDefaultCell().setBorder(Rectangle.TOP|Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
        tabLineas.getDefaultCell().setPadding(3);
        tabLineas.getDefaultCell().setUseAscender(true);
        tabLineas.getDefaultCell().setUseDescender(true);
        tabLineas.getDefaultCell().setColspan(4);
        tabLineas.getDefaultCell().setBackgroundColor(Color.decode("#20A627")); 
        tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabLineas.addCell("TOTAL: ");
        tabLineas.getDefaultCell().setColspan(1); 
        tabLineas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tabLineas.addCell("9999.99");
        return tabLineas;
    }
  
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new TestPDF2().createPdf(RESULT);
    }
}