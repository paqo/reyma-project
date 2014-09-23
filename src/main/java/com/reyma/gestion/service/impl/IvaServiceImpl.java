package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.service.IvaService;

@Service
@Transactional
public class IvaServiceImpl implements IvaService {

	public long countAllIvas() {
        return Iva.countIvas();
    }

	public void deleteIva(Iva iva) {
        iva.remove();
    }

	public Iva findIva(Integer id) {
        return Iva.findIva(id);
    }

	public List<Iva> findAllIvas() {
        return Iva.findAllIvas();
    }

	public List<Iva> findIvaEntries(int firstResult, int maxResults) {
        return Iva.findIvaEntries(firstResult, maxResults);
    }

	public void saveIva(Iva iva) {
        iva.persist();
    }

	public Iva updateIva(Iva iva) {
        return iva.merge();
    }
}
