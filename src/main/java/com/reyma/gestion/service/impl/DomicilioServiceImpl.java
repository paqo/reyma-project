package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.service.DomicilioService;

@Service
@Transactional
public class DomicilioServiceImpl implements DomicilioService {

	public long countAllDomicilios() {
        return Domicilio.countDomicilios();
    }

	public void deleteDomicilio(Domicilio domicilio) {
        domicilio.remove();
    }

	public Domicilio findDomicilio(Integer id) {
        return Domicilio.findDomicilio(id);
    }

	public List<Domicilio> findAllDomicilios() {
        return Domicilio.findAllDomicilios();
    }

	public List<Domicilio> findDomicilioEntries(int firstResult, int maxResults) {
        return Domicilio.findDomicilioEntries(firstResult, maxResults);
    }

	public void saveDomicilio(Domicilio domicilio) {
        domicilio.persist();
    }

	public Domicilio updateDomicilio(Domicilio domicilio) {
        return domicilio.merge();
    }

	@Override
	public Domicilio findDomicilio(Domicilio domicilio) {
		return Domicilio.findDomicilio(domicilio);
	}
}
