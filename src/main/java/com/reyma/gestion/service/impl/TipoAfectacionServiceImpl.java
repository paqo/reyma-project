package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.service.TipoAfectacionService;

@Service
@Transactional
public class TipoAfectacionServiceImpl implements TipoAfectacionService {

	public long countAllTipoAfectacions() {
        return TipoAfectacion.countTipoAfectacions();
    }

	public void deleteTipoAfectacion(TipoAfectacion tipoAfectacion) {
        tipoAfectacion.remove();
    }

	public TipoAfectacion findTipoAfectacion(Integer id) {
        return TipoAfectacion.findTipoAfectacion(id);
    }

	public List<TipoAfectacion> findAllTipoAfectacions() {
        return TipoAfectacion.findAllTipoAfectacions();
    }

	public List<TipoAfectacion> findTipoAfectacionEntries(int firstResult, int maxResults) {
        return TipoAfectacion.findTipoAfectacionEntries(firstResult, maxResults);
    }

	public void saveTipoAfectacion(TipoAfectacion tipoAfectacion) {
        tipoAfectacion.persist();
    }

	public TipoAfectacion updateTipoAfectacion(TipoAfectacion tipoAfectacion) {
        return tipoAfectacion.merge();
    }

	public TipoAfectacion findTipoAfectacionByDesc(String tipo) {
		 return TipoAfectacion.findTipoAfectacion(tipo);
	}
}
