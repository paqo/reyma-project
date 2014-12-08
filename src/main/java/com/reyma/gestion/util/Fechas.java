package com.reyma.gestion.util;

import java.text.SimpleDateFormat;

public class Fechas {

	public static final String FORMATO_FECHA_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
	public static final String FORMATO_FECHA_DDMMYYYY = "dd/MM/yyyy";
	public static final SimpleDateFormat FORMATEADOR_DDMMYYYYHHMM;
	
	static {
		FORMATEADOR_DDMMYYYYHHMM = new SimpleDateFormat(FORMATO_FECHA_DDMMYYYYHHMM);
	}
}
