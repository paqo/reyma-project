package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Trabajo;
import com.reyma.gestion.service.TrabajoService;

@Service
@Transactional
public class TrabajoServiceImpl implements TrabajoService {

	public long countAllTrabajoes() {
        return Trabajo.countTrabajoes();
    }

	public void deleteTrabajo(Trabajo trabajo) {
        trabajo.remove();
    }

	public Trabajo findTrabajo(Integer id) {
        return Trabajo.findTrabajo(id);
    }

	public List<Trabajo> findAllTrabajoes() {
        return Trabajo.findAllTrabajoes();
    }
	
	public List<Trabajo> findTrabajosByIdSiniestro(Integer id) {
		return Trabajo.findTrabajosByIdSiniestro(id);
	}

	public List<Trabajo> findTrabajoEntries(int firstResult, int maxResults) {
        return Trabajo.findTrabajoEntries(firstResult, maxResults);
    }

	public void saveTrabajo(Trabajo trabajo) {
        trabajo.persist();
    }

	public Trabajo updateTrabajo(Trabajo trabajo) {
        return trabajo.merge();
    }
	
}
