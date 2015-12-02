package com.reyma.gestion.util;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class UtilsPDF {
	
	private static Logger logger = Logger.getLogger( UtilsPDF.class );
	
	public static Font FUENTE_CABECERA_AFECTADOS_TITULO;
    public static final Font FUENTE_CABECERA_AFECTADOS_DATOS_PRES = // "num prresupuesto/Factura.." 
    		FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255));
    public static final Font FUENTE_CABECERA_AFECTADOS_DATOS_AFEC = // "nombre,direccion.." 
    		FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255));
    public static final Font FUENTE_CABECERA_COLS = 
    		FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
    
    static {
    	try {
    		Resource resource = new ClassPathResource("com/reyma/gestion/tests/arial.ttf");
    		FUENTE_CABECERA_AFECTADOS_TITULO = 
    				new Font(BaseFont.createFont(resource.getFile().getAbsolutePath(), 
					BaseFont.WINANSI, BaseFont.EMBEDDED), 
					14, Font.NORMAL, Color.BLACK);
		} catch (Exception e) {
			logger.error("error cargando fichero de fuente", e);
		}
    }

}
