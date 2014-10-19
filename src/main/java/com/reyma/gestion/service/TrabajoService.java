package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Trabajo;

public interface TrabajoService {

	public abstract long countAllTrabajoes();


	public abstract void deleteTrabajo(Trabajo trabajo);


	public abstract Trabajo findTrabajo(Integer id);


	public abstract List<Trabajo> findAllTrabajoes();
	
	
	public abstract List<Trabajo> findTrabajosByIdSiniestro(Integer id);


	public abstract List<Trabajo> findTrabajoEntries(int firstResult, int maxResults);


	public abstract void saveTrabajo(Trabajo trabajo);


	public abstract Trabajo updateTrabajo(Trabajo trabajo);

}
