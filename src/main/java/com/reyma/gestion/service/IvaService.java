package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Iva;

public interface IvaService {

	public abstract long countAllIvas();


	public abstract void deleteIva(Iva iva);


	public abstract Iva findIva(Integer id);


	public abstract List<Iva> findAllIvas();


	public abstract List<Iva> findIvaEntries(int firstResult, int maxResults);


	public abstract void saveIva(Iva iva);


	public abstract Iva updateIva(Iva iva);

}
