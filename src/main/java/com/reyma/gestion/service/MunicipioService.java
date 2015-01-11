package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.ui.AutocompleteJSONBean;

public interface MunicipioService {

	public abstract long countAllMunicipios();


	public abstract void deleteMunicipio(Municipio municipio);


	public abstract Municipio findMunicipio(Integer id);


	public abstract List<Municipio> findAllMunicipios();
	
	
	public abstract List<Municipio> findAllMunicipiosByIdProvincia(Integer id);
	
	
	public abstract List<AutocompleteJSONBean> findMunicipiosParaAutocomplete(Integer id, String desc);
	
	
	public abstract Municipio findMunicipioByIdProvinciaAndDesc(Integer id, String desc);
	
	
	public abstract List<Municipio> findMunicipiosByIdProvAndDesc(Integer idProvincia, String descripcion);


	public abstract List<Municipio> findMunicipioEntries(int firstResult, int maxResults);


	public abstract void saveMunicipio(Municipio municipio);


	public abstract Municipio updateMunicipio(Municipio municipio);

}
