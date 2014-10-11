package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;

@Service
@Transactional
public class AfectadoDomicilioSiniestroServiceImpl implements AfectadoDomicilioSiniestroService {

	public long countAllAfectadoDomicilioSiniestroes() {
        return AfectadoDomicilioSiniestro.countAfectadoDomicilioSiniestroes();
    }

	public void deleteAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        afectadoDomicilioSiniestro.remove();
    }

	public AfectadoDomicilioSiniestro findAfectadoDomicilioSiniestro(Integer id) {
        return AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestro(id);
    }

	public List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes() {
        return AfectadoDomicilioSiniestro.findAllAfectadoDomicilioSiniestroes();
    }
	
	public List<AfectadoDomicilioSiniestro> findAfectadosDomicilioBySiniestro(Integer idSiniestro){
		return AfectadoDomicilioSiniestro.findAfectadosDomicilioBySiniestro(idSiniestro);
	}

	public List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults) {
        return AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestroEntries(firstResult, maxResults);
    }

	public void saveAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        afectadoDomicilioSiniestro.persist();
    }

	public AfectadoDomicilioSiniestro updateAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        return afectadoDomicilioSiniestro.merge();
    }
}
