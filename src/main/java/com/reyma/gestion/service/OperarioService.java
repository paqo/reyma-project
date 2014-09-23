package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Operario;

public interface OperarioService {

	public abstract long countAllOperarios();


	public abstract void deleteOperario(Operario operario);


	public abstract Operario findOperario(Integer id);


	public abstract List<Operario> findAllOperarios();


	public abstract List<Operario> findOperarioEntries(int firstResult, int maxResults);


	public abstract void saveOperario(Operario operario);


	public abstract Operario updateOperario(Operario operario);

}
