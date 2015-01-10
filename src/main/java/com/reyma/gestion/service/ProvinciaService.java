package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Provincia;

public interface ProvinciaService {

	public abstract long countAllProvincias();


	public abstract void deleteProvincia(Provincia provincia);


	public abstract Provincia findProvincia(Integer id);
	
	
	public abstract Provincia findProvinciaByDescripcion(String desc, boolean sensitive);


	public abstract List<Provincia> findAllProvincias();


	public abstract List<Provincia> findProvinciaEntries(int firstResult, int maxResults);


	public abstract void saveProvincia(Provincia provincia);


	public abstract Provincia updateProvincia(Provincia provincia);

}
