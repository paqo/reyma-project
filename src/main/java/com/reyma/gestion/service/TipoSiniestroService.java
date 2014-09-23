package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.TipoSiniestro;

public interface TipoSiniestroService {

	public abstract long countAllTipoSiniestroes();


	public abstract void deleteTipoSiniestro(TipoSiniestro tipoSiniestro);


	public abstract TipoSiniestro findTipoSiniestro(Integer id);


	public abstract List<TipoSiniestro> findAllTipoSiniestroes();


	public abstract List<TipoSiniestro> findTipoSiniestroEntries(int firstResult, int maxResults);


	public abstract void saveTipoSiniestro(TipoSiniestro tipoSiniestro);


	public abstract TipoSiniestro updateTipoSiniestro(TipoSiniestro tipoSiniestro);

}
