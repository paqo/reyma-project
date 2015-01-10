package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Provincia;
import com.reyma.gestion.service.ProvinciaService;

@Service
@Transactional
public class ProvinciaServiceImpl implements ProvinciaService {

	public long countAllProvincias() {
        return Provincia.countProvincias();
    }

	public void deleteProvincia(Provincia provincia) {
        provincia.remove();
    }

	public Provincia findProvincia(Integer id) {
        return Provincia.findProvincia(id);
    }

	public List<Provincia> findAllProvincias() {
        return Provincia.findAllProvincias();
    }

	public List<Provincia> findProvinciaEntries(int firstResult, int maxResults) {
        return Provincia.findProvinciaEntries(firstResult, maxResults);
    }

	public void saveProvincia(Provincia provincia) {
        provincia.persist();
    }

	public Provincia updateProvincia(Provincia provincia) {
        return provincia.merge();
    }

	public Provincia findProvinciaByDescripcion(String desc, boolean sensitive) {
		try {
			return Provincia.findProvinciaByDescripcion(desc, sensitive);
		} catch (Exception e) {
			return null;
		}
	}
}
