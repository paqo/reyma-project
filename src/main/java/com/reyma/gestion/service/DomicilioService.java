package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Domicilio;

public interface DomicilioService {

	public abstract long countAllDomicilios();


	public abstract void deleteDomicilio(Domicilio domicilio);


	public abstract Domicilio findDomicilio(Integer id);


	public abstract List<Domicilio> findAllDomicilios();


	public abstract List<Domicilio> findDomicilioEntries(int firstResult, int maxResults);


	public abstract void saveDomicilio(Domicilio domicilio);


	public abstract Domicilio updateDomicilio(Domicilio domicilio);

}
