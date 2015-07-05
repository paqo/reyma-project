package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.LineaFactura;

public interface LineaFacturaService {

	public abstract long countAllLineaFacturas();


	public abstract void deleteLineaFactura(LineaFactura lineaFactura);


	public abstract LineaFactura findLineaFactura(Integer id);
	
	
	public abstract List<LineaFactura> findLineasFacturaByIdFactura(Integer idFactura);


	public abstract List<LineaFactura> findAllLineaFacturas();


	public abstract List<LineaFactura> findLineaFacturaEntries(int firstResult, int maxResults);


	public abstract void saveLineaFactura(LineaFactura lineaFactura);


	public abstract LineaFactura updateLineaFactura(LineaFactura lineaFactura);

}
