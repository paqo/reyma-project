package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Compania;
import com.reyma.gestion.service.CompaniaService;

@Service
@Transactional
public class CompaniaServiceImpl implements CompaniaService {

	public long countAllCompanias() {
        return Compania.countCompanias();
    }

	public void deleteCompania(Compania compania) {
        compania.remove();
    }

	public Compania findCompania(Integer id) {
        return Compania.findCompania(id);
    }

	public List<Compania> findAllCompanias() {
        return Compania.findAllCompanias();
    }

	public List<Compania> findCompaniaEntries(int firstResult, int maxResults) {
        return Compania.findCompaniaEntries(firstResult, maxResults);
    }

	public void saveCompania(Compania compania) {
        compania.persist();
    }

	public Compania updateCompania(Compania compania) {
        return compania.merge();
    }
	
	public Compania findCompaniaByDesc(String desc) {
		return Compania.findCompaniaByDesc(desc);
	}
}
