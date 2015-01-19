package com.reyma.gestion.service;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;

public interface SiniestroService {

	public abstract long countAllSiniestroes();


	public abstract void deleteSiniestro(Siniestro siniestro);


	public abstract Siniestro findSiniestro(Integer id);


	public abstract List<Siniestro> findAllSiniestroes();
	
	
	public abstract List<Siniestro> findSiniestrosCaducados(Integer dias);
	
	
	public abstract List<Siniestro> findAllSiniestroes(String sortField, String sortOrder);


	public abstract List<Siniestro> findSiniestroEntries(int firstResult, int maxResults);
	
	
	public abstract List<Siniestro> findSiniestroEntries(int firstResult, int maxResults, String sortField, String sortOrder);


	public abstract void saveSiniestro(Siniestro siniestro);


	public abstract Siniestro updateSiniestro(Siniestro siniestro);


	public abstract Siniestro findSiniestroByNumSiniestro(String sinNumero);
	
	
	public List<Siniestro> findSiniestrosParaFecha(Calendar fecha);
	
	
	public abstract List<Siniestro> buscarSiniestrosPorCriterios(Siniestro siniestro, Domicilio domicilio, Persona persona, Map<String, Object> params);
}
