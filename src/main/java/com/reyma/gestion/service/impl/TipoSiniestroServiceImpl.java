package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.TipoSiniestro;
import com.reyma.gestion.service.TipoSiniestroService;

@Service
@Transactional
public class TipoSiniestroServiceImpl implements TipoSiniestroService {

	public long countAllTipoSiniestroes() {
        return TipoSiniestro.countTipoSiniestroes();
    }

	public void deleteTipoSiniestro(TipoSiniestro tipoSiniestro) {
        tipoSiniestro.remove();
    }

	public TipoSiniestro findTipoSiniestro(Integer id) {
        return TipoSiniestro.findTipoSiniestro(id);
    }

	public List<TipoSiniestro> findAllTipoSiniestroes() {
        return TipoSiniestro.findAllTipoSiniestroes();
    }

	public List<TipoSiniestro> findTipoSiniestroEntries(int firstResult, int maxResults) {
        return TipoSiniestro.findTipoSiniestroEntries(firstResult, maxResults);
    }

	public void saveTipoSiniestro(TipoSiniestro tipoSiniestro) {
        tipoSiniestro.persist();
    }

	public TipoSiniestro updateTipoSiniestro(TipoSiniestro tipoSiniestro) {
        return tipoSiniestro.merge();
    }
}
