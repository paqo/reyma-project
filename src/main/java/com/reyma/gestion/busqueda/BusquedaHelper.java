package com.reyma.gestion.busqueda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	
	public List<Siniestro> buscar(Siniestro siniestro, Domicilio domicilio, Persona persona, Map<String, Object> parametrosAdicionales) {
		List<Siniestro> res = new ArrayList<Siniestro>();
		
		res = siniestroService.buscarSiniestrosPorCriterios(siniestro, domicilio, 
				persona, parametrosAdicionales);
		
		return res;
	}
	
	public List<Siniestro> buscarSiniestrosParaFecha(Calendar fecha) {		
		return siniestroService.findSiniestrosParaFecha(fecha);
	}
	
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

	public Map<String, Object> obtenerParametrosAdicionales(
			HttpServletRequest request) {
		
		Map<String, Object> params = new HashMap<String, Object>();     
		
		// fecha ini
		Calendar cal = obtenerCalendarDesdeRequest(request, "fechaIni");
		if ( cal != null ){
			params.put("fechaIni", cal);
		}
		// fecha fin
		cal = obtenerCalendarDesdeRequest(request, "fechaFin");
		if ( cal != null ){
			params.put("fechaFin", cal);
		}
		return params;
	}

	private Calendar obtenerCalendarDesdeRequest(HttpServletRequest request, String nombreParam) {
		if ( request.getParameter(nombreParam) == null ){
			return null;
		}
		
		Calendar cal1 = null;
		try {
			String _f1 = request.getParameter(nombreParam);
			String[] chunks = _f1.split("\\/");
			cal1 = new GregorianCalendar(Integer.parseInt(chunks[2]), 
					Integer.parseInt(chunks[1]) - 1, /* 0-based */
					Integer.parseInt(chunks[0]) );
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cal1 = null;
		}
		return cal1;
	}
}
