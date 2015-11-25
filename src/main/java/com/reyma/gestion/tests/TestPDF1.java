package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.dao.LineaPresupuesto;
  
public class TestPDF1 implements PdfPTableEvent {
 
	/** The resulting PDF file. */
    public static final String RESULT = "c:/temp/test_c.pdf";
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException     
     */
    public void createPdf(String filename) throws DocumentException, IOException {
         
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        
        PdfPTableEvent event = new TestPDF1();
        
        List<LineaPresupuesto> lineas = getLineas();
        int paginas = 3;
        for (int i=0;i<paginas;i++) {
            PdfPTable table = getTable(lineas);
            table.setTableEvent(event);
            document.add(table);
            document.newPage();
        }
        // step 5
        document.close();
        System.out.println("terminado!");
        System.exit(0);
    }
    
    
 
    private List<LineaPresupuesto> getLineas() {	
		return new ArrayList<LineaPresupuesto>(
					Arrays.asList(new LineaPresupuesto[]{
							getLineaRandom(),
							getLineaRandom(),
							getLineaRandom(),
							getLineaRandom()
					})
				);
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
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });        
        table.setWidthPercentage(100f);
        // cabecera columnas
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(Color.decode("#69A322"));    	     
        table.addCell("Descripcion");
        table.addCell("Base");
        table.addCell("%");
        table.addCell("IVA");        
        table.addCell("Subtotal");
        // oficio 1
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(Color.decode("#20A627")); 
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell("ALBAÑILERIA");
        table.getDefaultCell().setBackgroundColor(null);  
        table.getDefaultCell().setColspan(1);
        // 3 lineas especiales
        table.setHeaderRows(3);
        // de las cuales, una el pie
        table.setFooterRows(1);
        // lineas
        for (LineaPresupuesto linea : lineas) {
        	table.getDefaultCell().setBorder(Rectangle.LEFT);
        	table.addCell(linea.getLinConcepto());
        	table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(linea.getLinImporte().toString());
            table.addCell(String.valueOf(linea.getLinIvaId().getIvaValor()));
            table.addCell(RandomStringUtils.randomNumeric(2));
            table.getDefaultCell().setBorder(Rectangle.RIGHT);
            table.addCell(RandomStringUtils.randomNumeric(4));
		}    
        return table;
    }
 
    /**
     * Draws a background for every other row.
     * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(
     *      com.itextpdf.text.pdf.PdfPTable, float[][], float[], int, int,
     *      com.itextpdf.text.pdf.PdfContentByte[])
     */
    public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
        int headerRows, int rowStart, PdfContentByte[] canvases) {
        int columns;
        Rectangle rect;
        int footer = widths.length - table.getFooterRows();
        int header = table.getHeaderRows() - table.getFooterRows() + 1;
        for (int row = header; row < footer; row += 2) {
            columns = widths[row].length - 1;
            rect = new Rectangle(widths[row][0], heights[row],
                        widths[row][columns], heights[row + 1]);
            rect.setBackgroundColor(Color.WHITE);
            rect.setBorder(Rectangle.NO_BORDER);
            canvases[PdfPTable.BASECANVAS].rectangle(rect);
        }
    }
 
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new TestPDF1().createPdf(RESULT);
    }
}