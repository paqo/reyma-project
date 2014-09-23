package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Oficio;

public interface OficioService {

	public abstract long countAllOficios();


	public abstract void deleteOficio(Oficio oficio);


	public abstract Oficio findOficio(Integer id);


	public abstract List<Oficio> findAllOficios();


	public abstract List<Oficio> findOficioEntries(int firstResult, int maxResults);


	public abstract void saveOficio(Oficio oficio);


	public abstract Oficio updateOficio(Oficio oficio);

}
