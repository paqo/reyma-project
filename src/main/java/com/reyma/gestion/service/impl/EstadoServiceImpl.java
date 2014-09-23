package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Estado;
import com.reyma.gestion.service.EstadoService;

@Service
@Transactional
public class EstadoServiceImpl implements EstadoService {

	public long countAllEstadoes() {
        return Estado.countEstadoes();
    }

	public void deleteEstado(Estado estado) {
        estado.remove();
    }

	public Estado findEstado(Integer id) {
        return Estado.findEstado(id);
    }

	public List<Estado> findAllEstadoes() {
        return Estado.findAllEstadoes();
    }

	public List<Estado> findEstadoEntries(int firstResult, int maxResults) {
        return Estado.findEstadoEntries(firstResult, maxResults);
    }

	public void saveEstado(Estado estado) {
        estado.persist();
    }

	public Estado updateEstado(Estado estado) {
        return estado.merge();
    }
}
