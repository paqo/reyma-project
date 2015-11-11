package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.service.PresupuestoService;

@Service
@Transactional
public class PresupuestoServiceImpl implements PresupuestoService {


	public void deletePresupuesto(Presupuesto factura) {
        factura.remove();
    }

	public Presupuesto findPresupuesto(Integer id) {
        return Presupuesto.findPresupuesto(id);
    }

	public List<Presupuesto> findAllPresupuestos() {
        return Presupuesto.findAllPresupuestos();
    }

	public List<Presupuesto> findPresupuestoEntries(int firstResult, int maxResults) {
        return Presupuesto.findPresupuestoEntries(firstResult, maxResults);
    }

	public void savePresupuesto(Presupuesto factura) {
        factura.persist();
    }

	public Presupuesto updatePresupuesto(Presupuesto factura) {
        return factura.merge();
    }

	public List<Presupuesto> findPresupuestosByIdSiniestro(Integer id) {
		return Presupuesto.findPresupuestoParaSiniestro(id);
	}

	public long countAllpresupuestos() {
		return Presupuesto.countPresupuesto();
	}

	@Override
	public List<Presupuesto> findPresupuestoByIdSiniestro(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
}
