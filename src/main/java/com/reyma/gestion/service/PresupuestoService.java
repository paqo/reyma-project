package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Presupuesto;

public interface PresupuestoService {

	public abstract long countAllpresupuestos();


	public abstract void deletePresupuesto(Presupuesto presupuesto);


	public abstract Presupuesto findPresupuesto(Integer id);


	public abstract List<Presupuesto> findAllPresupuestos();
	
	
	public abstract List<Presupuesto> findPresupuestoByIdSiniestro(Integer id);


	public abstract List<Presupuesto> findPresupuestoEntries(int firstResult, int maxResults);


	public abstract void savePresupuesto(Presupuesto presupuesto);


	public abstract Presupuesto updatePresupuesto(Presupuesto presupuesto);

}
