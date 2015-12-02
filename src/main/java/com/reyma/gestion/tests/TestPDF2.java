package com.reyma.gestion.tests;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.dao.LineaPresupuesto;
  
public class TestPDF2 {
 
	/** The resulting PDF file. */
    public static final String RESULT = "c:/temp/test_c.pdf";
    
   /* public static Font FUENTE_CABECERA_AFECTADOS_TITULO = // "Presupuesto/Factura" 
    		FontFactory.getFont(FontFactory.HELVETICA, 15, Color.BLACK); */
    public static Font FUENTE_CABECERA_AFECTADOS_TITULO;
    public static final Font FUENTE_CABECERA_AFECTADOS_DATOS_PRES = // "num prresupuesto/Factura.." 
    		FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255));
    public static final Font FUENTE_CABECERA_AFECTADOS_DATOS_AFEC = // "nombre,direccion.." 
    		FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255));
    public static final Font FUENTE_CABECERA_COLS = 
    		FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
    
    static {
    	try {
    		FUENTE_CABECERA_AFECTADOS_TITULO = 
    				new Font(BaseFont.createFont("C:/Users/Fcamarena/git/reyma-project/src/main/java/" +
    			    "com/reyma/gestion/tests/arial.ttf", 
					BaseFont.WINANSI, BaseFont.EMBEDDED), 
					14, Font.NORMAL, Color.BLACK);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException     
     */
    public void createPdf(String filename) throws DocumentException, IOException {
         
    	CabeceraPie cabeceraPie = new CabeceraPie();
        // step 1
        Document document = new Document(PageSize.A4);              
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setBoxSize("art", new Rectangle(36, 40, 559, 788));
        writer.setPageEvent(cabeceraPie);       
        // step 3
        document.open();
        // step 4
        PdfPTable tabla = getTablaDatosAfectado();
        tabla.setSpacingBefore(15);
        tabla.setSpacingAfter(10);   
        document.add(tabla);                
        // step 5
        PdfPTable tablaLineas = getTable(getLineas());
        document.add(tablaLineas);        
        // step 6
        cabeceraPie.setContentLeft(false);
        document.close();
        System.out.println("terminado!");
        System.exit(0);
    }
    
	private List<LineaPresupuesto> getLineas() {
    	List<LineaPresupuesto> listaTest = new ArrayList<LineaPresupuesto>();
    	for ( int i=0; i<5; i++ ){
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
	

	private PdfPTable getTablaDatosAfectado() {
		PdfPTable tabDatosAfectados = new PdfPTable(new float[] { 1, 1});
		
		tabDatosAfectados.setWidthPercentage(100f);
		
		Paragraph datosTitulo = new Paragraph();		
		datosTitulo.add(new Chunk("PRESUPUESTO", FUENTE_CABECERA_AFECTADOS_TITULO));
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(new Chunk("Núm. encargo: 9927085528"));
		datosTitulo.add(Chunk.NEWLINE); 
		datosTitulo.add(new Chunk("Núm. presupuesto: 1427211305555"));
		
    	tabDatosAfectados.getDefaultCell().setBorder(Rectangle.NO_BORDER);		
    	tabDatosAfectados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT|Element.ALIGN_TOP);
    	tabDatosAfectados.addCell(datosTitulo);
    	
    	tabDatosAfectados.getDefaultCell().setPadding(5);
    	tabDatosAfectados.getDefaultCell().setBorderWidth(1);
    	tabDatosAfectados.getDefaultCell().setBorder(Rectangle.TOP|Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
    	tabDatosAfectados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    	
    	Paragraph datosAfectado = new Paragraph();
    	datosAfectado.add(new Chunk("My Cousin Sister"));
    	datosAfectado.add(Chunk.NEWLINE); 
    	datosAfectado.add(new Chunk("C/ My Precious, Sevilla, (Sevilla)")); 
    	datosAfectado.add(Chunk.NEWLINE);  
    	datosAfectado.add(new Chunk("CP: 23456"));
    	datosAfectado.add(Chunk.NEWLINE);  
    	datosAfectado.add(new Chunk("NIF: 12345678P"));    	
    	tabDatosAfectados.addCell(datosAfectado);
    	
		return tabDatosAfectados;
	}
	
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