package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.service.MunicipioService;

@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

	public long countAllMunicipios() {
        return Municipio.countMunicipios();
    }

	public void deleteMunicipio(Municipio municipio) {
        municipio.remove();
    }

	public Municipio findMunicipio(Integer id) {
        return Municipio.findMunicipio(id);
    }

	public List<Municipio> findAllMunicipios() {
        return Municipio.findAllMunicipios();
    }

	public List<Municipio> findMunicipioEntries(int firstResult, int maxResults) {
        return Municipio.findMunicipioEntries(firstResult, maxResults);
    }

	public void saveMunicipio(Municipio municipio) {
        municipio.persist();
    }

	public Municipio updateMunicipio(Municipio municipio) {
        return municipio.merge();
    }

	public List<Municipio> findAllMunicipiosByIdProvincia(Integer idProvincia) {
		return Municipio.findAllMunicipiosByIdProvincia(idProvincia);
	}
}
