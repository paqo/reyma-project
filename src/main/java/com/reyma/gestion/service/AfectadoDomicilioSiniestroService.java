package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;

public interface AfectadoDomicilioSiniestroService {

	public abstract long countAllAfectadoDomicilioSiniestroes();


	public abstract void deleteAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);


	public abstract AfectadoDomicilioSiniestro findAfectadoDomicilioSiniestro(Integer id);


	public abstract List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes();


	public abstract List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults);


	public abstract void saveAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);


	public abstract AfectadoDomicilioSiniestro updateAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro);

}
