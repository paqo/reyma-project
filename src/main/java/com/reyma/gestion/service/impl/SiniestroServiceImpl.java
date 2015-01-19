package com.reyma.gestion.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Compania;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
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
	
	public Siniestro findSiniestroByNumSiniestro(String numSiniestro) {
        return Siniestro.findSiniestroByNumSiniestro(numSiniestro);
    }

	public List<Siniestro> findAllSiniestroes() {
        return Siniestro.findAllSiniestroes();
    }
	
	public List<Siniestro> findSiniestrosCaducados(Integer dias) {
		// de momento solo para AXA y 20 dias inicialmente
		if ( dias == null ){
			dias = 20;
		}
		Compania compania = Compania.findCompaniaByDesc("AXA");	
		List<Siniestro> encontrados = Siniestro.findSiniestrosCaducados(dias, compania);		
		return encontrados;
	}
	
	public List<Siniestro> findSiniestrosParaFecha(Calendar fecha) {
		return Siniestro.findSiniestrosParaFecha(fecha);
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

	@Override
	public List<Siniestro> buscarSiniestrosPorCriterios(Siniestro siniestro,
			Domicilio domicilio, Persona persona, Map<String, Object> params) {
		return Siniestro.buscarSiniestrosPorCriterios(siniestro, domicilio, persona, params);
	}

	
}
