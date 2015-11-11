package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.LineaPresupuesto;

public interface LineaPresupuestoService {

	public abstract long countAllLineaFacturas();


	public abstract void deleteLineaPresupuesto(LineaPresupuesto lineaPresupuesto);


	public abstract LineaPresupuesto findLineaPresupuesto(Integer id);
	
	
	public abstract List<LineaPresupuesto> findLineasPresupuestoByIdPresupuesto(Integer idPresupuesto);


	public abstract List<LineaPresupuesto> findAllLineaFacturas();


	public abstract List<LineaPresupuesto> findLineaPresupuestoEntries(int firstResult, int maxResults);


	public abstract void saveLineaPresupuesto(LineaPresupuesto lineaPresupuesto);


	public abstract LineaPresupuesto updateLineaPresupuesto(LineaPresupuesto lineaPresupuesto);

}
