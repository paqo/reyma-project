package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Siniestro;

public interface SiniestroService {

	public abstract long countAllSiniestroes();


	public abstract void deleteSiniestro(Siniestro siniestro);


	public abstract Siniestro findSiniestro(Integer id);


	public abstract List<Siniestro> findAllSiniestroes();
	
	
	public abstract List<Siniestro> findAllSiniestroes(String sortField, String sortOrder);


	public abstract List<Siniestro> findSiniestroEntries(int firstResult, int maxResults);
	
	
	public abstract List<Siniestro> findSiniestroEntries(int firstResult, int maxResults, String sortField, String sortOrder);


	public abstract void saveSiniestro(Siniestro siniestro);


	public abstract Siniestro updateSiniestro(Siniestro siniestro);


	public abstract Siniestro findSiniestroByNumSiniestro(String sinNumero);

}
