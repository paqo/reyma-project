package com.reyma.gestion.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {

	public static final String FORMATO_FECHA_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
	public static final String FORMATO_FECHA_DDMMYYYY = "dd/MM/yyyy";
	public static final SimpleDateFormat FORMATEADOR_DDMMYYYYHHMM;
	public static final SimpleDateFormat FORMATEADOR_DDMMYYYY;
	public static final double UN_DIA_MILISEGUNDOS = 1000 * 60 * 60 * 24;
	
	static {
		FORMATEADOR_DDMMYYYYHHMM = new SimpleDateFormat(FORMATO_FECHA_DDMMYYYYHHMM);
		FORMATEADOR_DDMMYYYY = new SimpleDateFormat(FORMATO_FECHA_DDMMYYYY);
	}
	
	public static Calendar getFechaHoy(boolean instanteInicial) {
		Calendar hoy = GregorianCalendar.getInstance();		
		if (instanteInicial){
			hoy.set(GregorianCalendar.HOUR_OF_DAY, 0);
			hoy.set(GregorianCalendar.MINUTE, 0);
			hoy.set(GregorianCalendar.SECOND, 0);
		}		
		return hoy;
	}
	
	public static int diasTranscurridosDesde(Date fechaDesde) {
		long hoyMilis = getFechaHoy(true).getTimeInMillis();
		long fechaDesdeMilis = fechaDesde.getTime();
		return new Double(Math.floor ((hoyMilis - fechaDesdeMilis) / UN_DIA_MILISEGUNDOS)).intValue();
	}
	
	public static int diasTranscurridosDesde(String fechaDesde) {
		try {
			return diasTranscurridosDesde(FORMATEADOR_DDMMYYYYHHMM.parse(fechaDesde + " 00:00"));
		} catch (ParseException e) {			
			return -1;
		}
	}
	
	public static String formatearFechaDDMMYYYY(Date fecha) {
		return FORMATEADOR_DDMMYYYY.format(fecha);		
	}
	
	public static String formatearFechaDDMMYYYYHHMM(Date fecha) {
		return FORMATEADOR_DDMMYYYYHHMM.format(fecha);		
	}

}
