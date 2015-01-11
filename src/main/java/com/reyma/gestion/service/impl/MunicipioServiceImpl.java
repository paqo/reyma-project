package com.reyma.gestion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.ui.AutocompleteJSONBean;

@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

	public long countAllMunicipios() {
        return Municipio.countMunicipios();
    }

	public void deleteMunicipio(Municipio municipio) {
        municipio.remove();
    }

	public Municipio findMunicipio(Integer id) {
        return Municipio.findMunicipio(id);
    }

	public List<Municipio> findAllMunicipios() {
        return Municipio.findAllMunicipios();
    }

	public List<Municipio> findMunicipioEntries(int firstResult, int maxResults) {
        return Municipio.findMunicipioEntries(firstResult, maxResults);
    }

	public void saveMunicipio(Municipio municipio) {
        municipio.persist();
    }

	public Municipio updateMunicipio(Municipio municipio) {
        return municipio.merge();
    }

	public List<Municipio> findAllMunicipiosByIdProvincia(Integer idProvincia) {
		return Municipio.findAllMunicipiosByIdProvincia(idProvincia);
	}

	public List<AutocompleteJSONBean> findMunicipiosParaAutocomplete(Integer idProvincia, String descripcion) {
		List<AutocompleteJSONBean> res = new ArrayList<AutocompleteJSONBean>();
		List<Municipio> municipios = Municipio.findMunicipiosByIdProvinciaAndDesc(idProvincia, descripcion);	
		for (Municipio municipio : municipios) {
			res.add(new AutocompleteJSONBean(
					municipio.getMunId().toString(), municipio.getMunDescripcion(), municipio.getMunDescripcion()));
		}		
		return res;
	}
	
	public List<Municipio> findMunicipiosByIdProvAndDesc(Integer idProvincia, String descripcion) {
		try {
			return Municipio.findMunicipiosByIdProvinciaAndDesc(idProvincia, descripcion);
		} catch (Exception e) {
			return null;
		}
	}	

	public Municipio findMunicipioByIdProvinciaAndDesc(Integer id, String descripcion) {
		try {
			return Municipio.findMunicipioByIdProvinciaAndDesc(id, descripcion);
		} catch (Exception e) {
			return null;
		}
	}	
}
