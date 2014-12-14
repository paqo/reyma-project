package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import com.reyma.gestion.util.Fechas;

import flexjson.JSONSerializer;

@RequestMapping("/busquedas")
@Controller
public class BusquedaController {

	@Autowired
	BusquedaHelper busquedas;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String buscar(Siniestro siniestro, Domicilio domicilio, Persona persona, Model uiModel, HttpServletRequest request) {
                
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
               
        Map<String, Object> parametrosAdicionales = busquedas.obtenerParametrosAdicionales(request);
        List<ResultadoBusqueda> resultados;
		try {
			List<Siniestro> sinEncontrados = busquedas.buscar(siniestro, domicilio, persona, parametrosAdicionales);
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

	void addDateTimeFormatPatterns(Model uiModel) {       
		uiModel.addAttribute("siniestro_sinfechacomunicacion_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
        uiModel.addAttribute("siniestro_sinfechaocurrencia_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
    }

	void populateEditForm(Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
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
