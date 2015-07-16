package com.reyma.gestion.util;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.integracion.IntegracionHelper;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.ui.listados.SiniestroListadoDataTablesDTO;

public class Utils {
	
	public static String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
		
	public static void cargarAfectadosYDomicilios(Siniestro sin, SiniestroListadoDataTablesDTO sinDto, AfectadoDomicilioSiniestroService adsService) {
		List<AfectadoDomicilioSiniestro> afectados = adsService.findAfectadosDomicilioByIdSiniestro( sin.getSinId() );
		StringBuilder sbAfec = new StringBuilder();
		StringBuilder sbDom = new StringBuilder();
		Set<Integer> domicilios = new HashSet<Integer>();
		Set<Integer> perjudicados = new HashSet<Integer>();
		for (AfectadoDomicilioSiniestro afectado : afectados) {
			if ( afectado.getAdsTafId().getTafId() == IntegracionHelper.TIPO_AFEC_AMBOS.getTafId() || 
				 afectado.getAdsTafId().getTafId() == IntegracionHelper.TIPO_AFEC_PERJUDICADO.getTafId() ){
				if ( !domicilios.contains( afectado.getAdsDomId().getDomId() ) ){
					sbDom.append( afectado.getAdsDomId().getDomDireccion() + ", " );
					domicilios.add(afectado.getAdsDomId().getDomId());
				}
				if ( !perjudicados.contains(afectado.getAdsPerId().getPerId()) ){
					sbAfec.append( afectado.getAdsPerId().getPerNombre() + ", " );
					perjudicados.add(afectado.getAdsPerId().getPerId());
				}
			}
		}
		
		String afectado = "", direccion = "";		
				
		if ( sbAfec.length() >= 2 ){ // es una lista real de afectados, distinto de ", "
			afectado = sbAfec.substring(0, sbAfec.length()-2);
		} else {
			afectado = afectados != null && !afectados.isEmpty()? 
					afectados.get(0).getAdsPerId().getPerNombre() :"";
		}
		
		if ( sbDom.length() >= 2 ){ // igual anterior
			direccion = sbDom.substring(0, sbDom.length()-2);
		} else {
			direccion = afectados != null && !afectados.isEmpty()? 
					afectados.get(0).getAdsDomId().getDomDireccion() :"";
		}		
		sinDto.setDireccion(direccion);
		sinDto.setAfectado(afectado);				
	}
}
