package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Factura;

public interface FacturaService {

	public abstract long countAllFacturas();


	public abstract void deleteFactura(Factura factura);


	public abstract Factura findFactura(Integer id);


	public abstract List<Factura> findAllFacturas();
	
	
	public abstract List<Factura> findFacturasByIdSiniestro(Integer id);


	public abstract List<Factura> findFacturaEntries(int firstResult, int maxResults);


	public abstract void saveFactura(Factura factura);


	public abstract Factura updateFactura(Factura factura);

}
