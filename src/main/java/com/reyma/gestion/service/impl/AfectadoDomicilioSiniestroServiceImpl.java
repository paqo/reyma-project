package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.PersonaService;

@Service
@Transactional
public class AfectadoDomicilioSiniestroServiceImpl implements AfectadoDomicilioSiniestroService {

	@Autowired
    PersonaService personaService;
	
	@Autowired
    DomicilioService domicilioService;
	
	@Autowired
	MunicipioService municipioService; 
	
	public long countAllAfectadoDomicilioSiniestroes() {
        return AfectadoDomicilioSiniestro.countAfectadoDomicilioSiniestroes();
    }

	public boolean deleteAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        try {
			afectadoDomicilioSiniestro.remove();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }

	public AfectadoDomicilioSiniestro findAfectadoDomicilioSiniestro(Integer id) {
        return AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestro(id);
    }

	public List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes() {
        return AfectadoDomicilioSiniestro.findAllAfectadoDomicilioSiniestroes();
    }
	
	public List<AfectadoDomicilioSiniestro> findAfectadosDomicilioByIdSiniestro(Integer idSiniestro){
		return AfectadoDomicilioSiniestro.findAfectadosDomicilioBySiniestro(idSiniestro);
	}
	
	public List<AfectadoDomicilioSiniestro> findAfectadosDomicilioByIdDomicilio(Integer idSiniestro){
		return AfectadoDomicilioSiniestro.findAfectadosDomicilioByDomicilio(idSiniestro);
	}
	
	public List<AfectadoDomicilioSiniestro> findAfectadosDomicilioByIdPersona(Integer idSiniestro){
		return AfectadoDomicilioSiniestro.findAfectadosDomicilioByPersona(idSiniestro);
	}

	public List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        return AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestroEntries(firstResult, maxResults, sortFieldName, sortOrder);
    }

	public void saveAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        afectadoDomicilioSiniestro.persist();
    }

	public AfectadoDomicilioSiniestro updateAfectadoDomicilioSiniestro(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        return afectadoDomicilioSiniestro.merge();
    }

	
	public List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes(
			String sortFieldName, String sortOrder) {
		return AfectadoDomicilioSiniestro.findAllAfectadoDomicilioSiniestroes(sortFieldName, sortOrder);
	}

	@Override
	public AfectadoDomicilioSiniestro findAfectadoParaFactura(
			Integer idSiniestro) {
		return AfectadoDomicilioSiniestro.findAfectadoParaFactura(idSiniestro);
	}	
}
