package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Oficio;
import com.reyma.gestion.service.OficioService;

@Service
@Transactional
public class OficioServiceImpl implements OficioService {

	public long countAllOficios() {
        return Oficio.countOficios();
    }

	public void deleteOficio(Oficio oficio) {
        oficio.remove();
    }

	public Oficio findOficio(Integer id) {
        return Oficio.findOficio(id);
    }

	public List<Oficio> findAllOficios() {
        return Oficio.findAllOficios();
    }

	public List<Oficio> findOficioEntries(int firstResult, int maxResults) {
        return Oficio.findOficioEntries(firstResult, maxResults);
    }

	public void saveOficio(Oficio oficio) {
        oficio.persist();
    }

	public Oficio updateOficio(Oficio oficio) {
        return oficio.merge();
    }
}
