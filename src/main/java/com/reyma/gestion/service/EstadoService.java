package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Estado;

public interface EstadoService {

	public abstract long countAllEstadoes();


	public abstract void deleteEstado(Estado estado);


	public abstract Estado findEstado(Integer id);
	
	
	public abstract Estado findEstadoByDescripcion(String descripcion);


	public abstract List<Estado> findAllEstadoes();


	public abstract List<Estado> findEstadoEntries(int firstResult, int maxResults);


	public abstract void saveEstado(Estado estado);


	public abstract Estado updateEstado(Estado estado);

}
