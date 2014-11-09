package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.SiniestroService;

@Service
@Transactional
public class SiniestroServiceImpl implements SiniestroService {

	public long countAllSiniestroes() {
        return Siniestro.countSiniestroes();
    }

	public void deleteSiniestro(Siniestro siniestro) {
        siniestro.remove();
    }

	public Siniestro findSiniestro(Integer id) {
        return Siniestro.findSiniestro(id);
    }

	public List<Siniestro> findAllSiniestroes() {
        return Siniestro.findAllSiniestroes();
    }

	public List<Siniestro> findSiniestroEntries(int firstResult, int maxResults) {
        return Siniestro.findSiniestroEntries(firstResult, maxResults);
    }

	public void saveSiniestro(Siniestro siniestro) {
        siniestro.persist();
    }

	public Siniestro updateSiniestro(Siniestro siniestro) {
        return siniestro.merge();
    }

	@Override
	public List<Siniestro> findAllSiniestroes(String sortField, String sortOrder) {
		return Siniestro.findAllSiniestroes(sortField, sortOrder);
	}

	@Override
	public List<Siniestro> findSiniestroEntries(int firstResult,
			int maxResults, String sortField, String sortOrder) {
		return Siniestro.findSiniestroEntries(firstResult, maxResults, sortField, sortOrder);
	}
}
