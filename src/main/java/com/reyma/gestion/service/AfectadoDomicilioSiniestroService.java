package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;

public interface AfectadoDomicilioSiniestroService {

	public abstract long countAllAfectadoDomicilioSiniestroes();


	public abstract boolean deleteAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);


	public abstract AfectadoDomicilioSiniestro findAfectadoDomicilioSiniestro(Integer id);


	public abstract List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes();
	
	
	public abstract List<AfectadoDomicilioSiniestro> findAfectadosDomicilioByIdSiniestro(Integer idSiniestro);
	
	
	public abstract List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder);
	
	
	public abstract List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes(String sortFieldName, String sortOrder);

	
	public abstract void saveAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);


	public abstract AfectadoDomicilioSiniestro updateAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);

}
