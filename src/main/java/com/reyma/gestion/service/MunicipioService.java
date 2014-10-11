package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Municipio;

public interface MunicipioService {

	public abstract long countAllMunicipios();


	public abstract void deleteMunicipio(Municipio municipio);


	public abstract Municipio findMunicipio(Integer id);


	public abstract List<Municipio> findAllMunicipios();
	
	public abstract List<Municipio> findAllMunicipiosByIdProvincia(Integer id);


	public abstract List<Municipio> findMunicipioEntries(int firstResult, int maxResults);


	public abstract void saveMunicipio(Municipio municipio);


	public abstract Municipio updateMunicipio(Municipio municipio);

}
