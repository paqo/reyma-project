package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.TipoAfectacion;

public interface TipoAfectacionService {

	public abstract long countAllTipoAfectacions();


	public abstract void deleteTipoAfectacion(TipoAfectacion tipoAfectacion);


	public abstract TipoAfectacion findTipoAfectacion(Integer id);
	
	
	public abstract TipoAfectacion findTipoAfectacionByDesc(String tipo);


	public abstract List<TipoAfectacion> findAllTipoAfectacions();


	public abstract List<TipoAfectacion> findTipoAfectacionEntries(int firstResult, int maxResults);


	public abstract void saveTipoAfectacion(TipoAfectacion tipoAfectacion);


	public abstract TipoAfectacion updateTipoAfectacion(TipoAfectacion tipoAfectacion);

}
