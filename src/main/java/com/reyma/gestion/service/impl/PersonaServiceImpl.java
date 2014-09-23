package com.reyma.gestion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.service.PersonaService;

@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

	public long countAllPersonae() {
        return Persona.countPersonae();
    }

	public void deletePersona(Persona persona) {
        persona.remove();
    }

	public Persona findPersona(Integer id) {
        return Persona.findPersona(id);
    }

	public List<Persona> findAllPersonae() {
        return Persona.findAllPersonae();
    }

	public List<Persona> findPersonaEntries(int firstResult, int maxResults) {
        return Persona.findPersonaEntries(firstResult, maxResults);
    }

	public void savePersona(Persona persona) {
        persona.persist();
    }

	public Persona updatePersona(Persona persona) {
        return persona.merge();
    }
}
