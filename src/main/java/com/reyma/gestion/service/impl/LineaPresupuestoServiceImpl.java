package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.service.LineaPresupuestoService;

@Service
@Transactional
public class LineaPresupuestoServiceImpl implements LineaPresupuestoService {

	public long countAllLineaFacturas() {
        return LineaPresupuesto.countLineasPresupuesto();
    }

	public void deleteLineaPresupuesto(LineaPresupuesto lineaPresupuesto) {
		lineaPresupuesto.remove();
    }

	public LineaPresupuesto findLineaPresupuesto(Integer id) {
        return LineaPresupuesto.findLineaPresupuesto(id);
    }

	public List<LineaPresupuesto> findAllLineasPresupuesto() {
        return LineaPresupuesto.findAllLineasPresupuesto();
    }

	public void saveLineaPresupuesto(LineaPresupuesto lineaPresupuesto) {
		lineaPresupuesto.persist();
    }

	public LineaPresupuesto updateLineaPresupuesto(LineaPresupuesto lineaPresupuesto) {
        return lineaPresupuesto.merge();
    }

	public List<LineaPresupuesto> findLineasPresupuestoByIdPresupuesto(Integer idPresupuesto) {
		return LineaPresupuesto.findLineaPresupuestoByIdPresupuesto(idPresupuesto);
	}

	public List<LineaPresupuesto> findLineaPresupuestoEntries(int firstResult,
			int maxResults) {
		return LineaPresupuesto.findLineaPresupuestoEntries(firstResult, maxResults);
	}

}
