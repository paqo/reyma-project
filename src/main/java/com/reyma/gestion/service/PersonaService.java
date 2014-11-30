package com.reyma.gestion.service;
import java.util.List;

import com.reyma.gestion.dao.Persona;

public interface PersonaService {

	public abstract long countAllPersonae();


	public abstract void deletePersona(Persona persona);


	public abstract Persona findPersona(Integer id);
	
	
	public abstract Persona findPersona(Persona persona);


	public abstract List<Persona> findAllPersonae();


	public abstract List<Persona> findPersonaEntries(int firstResult, int maxResults);


	public abstract void savePersona(Persona persona);


	public abstract Persona updatePersona(Persona persona);

}
