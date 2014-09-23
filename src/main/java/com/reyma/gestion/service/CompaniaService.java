package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Compania;

public interface CompaniaService {

	public abstract long countAllCompanias();


	public abstract void deleteCompania(Compania compania);


	public abstract Compania findCompania(Integer id);


	public abstract List<Compania> findAllCompanias();


	public abstract List<Compania> findCompaniaEntries(int firstResult, int maxResults);


	public abstract void saveCompania(Compania compania);


	public abstract Compania updateCompania(Compania compania);

}
