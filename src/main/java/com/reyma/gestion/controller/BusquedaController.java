package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.busqueda.BusquedaHelper;
import com.reyma.gestion.busqueda.dto.ResultadoBusqueda;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.EstadoService;

import flexjson.JSONSerializer;

@RequestMapping("/busquedas")
@Controller
public class BusquedaController {

	@Autowired
	BusquedaHelper busquedas;
	
	@Autowired
	CompaniaService companiaService;
	
	@Autowired
	EstadoService estadoService;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String buscar(Siniestro siniestro, Domicilio domicilio, Persona persona, Model uiModel, HttpServletRequest request) {
                
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
               
        Map<String, Object> parametrosAdicionales = busquedas.obtenerParametrosAdicionales(request);
        List<ResultadoBusqueda> resultados;
		try {
			List<Siniestro> sinEncontrados = busquedas.buscar(siniestro, domicilio, persona, parametrosAdicionales);
			if ( sinEncontrados.size() > busquedas.obtenerNumeroMaximoResultadosPermitidos() ){
				return busquedas.obtenerResultadoLimiteExcedido();
			}
			resultados = busquedas.obtenerResultadosBusqueda(sinEncontrados);
		} catch (Exception e) {			
			e.printStackTrace();
			resultados = new ArrayList<ResultadoBusqueda>();
		}       
        return serializer.exclude("*.class").serialize(resultados);        
    }
	
	/**
	 * Llamada desde la pagina inicial
	 * @param uiModel
	 * @param request
	 * @return
	 */
	@RequestMapping( value="inicio", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String inicio(Model uiModel, HttpServletRequest request) {
                
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
        String fechaStr = request.getParameter("fecha");
        List<ResultadoBusqueda> resultados;
        Calendar fecha = new GregorianCalendar(new Locale("es", "ES"));
		try {
			if ( "ayer".equalsIgnoreCase(fechaStr) ){
				// fecha de ayer
				fecha.add(Calendar.DAY_OF_MONTH, -1);
			}			
			fecha.set(Calendar.HOUR_OF_DAY, 0);
			fecha.set(Calendar.MINUTE, 0);
			fecha.set(Calendar.SECOND, 0);
			List<Siniestro> sinEncontrados = busquedas.buscarSiniestrosParaFecha(fecha);
			resultados = busquedas.obtenerResultadosBusqueda(sinEncontrados);
		} catch (Exception e) {			
			e.printStackTrace();
			resultados = new ArrayList<ResultadoBusqueda>();
		}       
        return serializer.exclude("*.class").serialize(resultados);        
    }
	
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel);
        return "busquedas/inicio";
    }

	void populateEditForm(Model uiModel) {	
		 uiModel.addAttribute("companias", companiaService.findAllCompanias());		
		 uiModel.addAttribute("estados", estadoService.findAllEstadoes());		
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
