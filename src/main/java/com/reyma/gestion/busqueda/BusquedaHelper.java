package com.reyma.gestion.busqueda;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyma.gestion.busqueda.dto.ResultadoBusqueda;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.SiniestroService;

@Service
public class BusquedaHelper {

	@Autowired
	private AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;
	
	@Autowired
	private SiniestroService siniestroService;
	
	public List<ResultadoBusqueda> obtenerResultadosBusqueda( List<Siniestro> siniestros ) {
		List<ResultadoBusqueda> res = new ArrayList<ResultadoBusqueda>();
		AfectadoDomicilioSiniestro ads;
		for (Siniestro siniestro : siniestros) {
			List<AfectadoDomicilioSiniestro> listaADS = 
			afectadoDomicilioSiniestroService.findAfectadosDomicilioByIdSiniestro(siniestro.getSinId());
			if ( listaADS != null && listaADS.size() != 0 ){
				ads = listaADS.get(0); // nos quedamos con el primero para mostrar en el listado
				res.add( new ResultadoBusqueda(siniestro, ads.getAdsDomId(), ads.getAdsPerId()) );
			}
		}		
		return res;
	}
	
	public List<Siniestro> buscar(Siniestro siniestro, Domicilio domicilio, Persona persona) {
		List<Siniestro> res = new ArrayList<Siniestro>();
		Siniestro aux;
		if ( !StringUtils.isEmpty(siniestro.getSinNumero()) ){
			aux = siniestroService.findSiniestroByNumSiniestro(siniestro.getSinNumero());
			res.add(aux);
		}
		return res;
	}
}
