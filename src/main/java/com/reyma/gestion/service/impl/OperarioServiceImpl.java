package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Operario;
import com.reyma.gestion.service.OperarioService;

@Service
@Transactional
public class OperarioServiceImpl implements OperarioService {

	public long countAllOperarios() {
        return Operario.countOperarios();
    }

	public void deleteOperario(Operario operario) {
        operario.remove();
    }

	public Operario findOperario(Integer id) {
        return Operario.findOperario(id);
    }

	public List<Operario> findAllOperarios() {
        return Operario.findAllOperarios();
    }

	public List<Operario> findOperarioEntries(int firstResult, int maxResults) {
        return Operario.findOperarioEntries(firstResult, maxResults);
    }

	public void saveOperario(Operario operario) {
        operario.persist();
    }

	public Operario updateOperario(Operario operario) {
        return operario.merge();
    }
}
