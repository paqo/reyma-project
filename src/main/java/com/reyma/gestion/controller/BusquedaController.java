package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.busqueda.BusquedaHelper;
import com.reyma.gestion.busqueda.dto.ResultadoBusqueda;
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
    public String buscar(Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	System.out.println("=> tiene errores.");      
        }
        
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
        
        /*
        List<ResultadoBusqueda> resultados = new ArrayList<ResultadoBusqueda>();
        Siniestro sinEnc = siniestroService.findSiniestroByNumSiniestro(siniestro.getSinNumero()); 
        
        List<AfectadoDomicilioSiniestro> adsEnc = afectadoDomicilioSiniestroService.
        		findAfectadosDomicilioByIdSiniestro(sinEnc.getSinId());
        if ( adsEnc.size() != 0 ){
        	resultados.add(new ResultadoBusqueda(sinEnc, adsEnc.get(0).getAdsDomId(), adsEnc.get(0).getAdsPerId()));
        	
        } */
        
        List<Siniestro> sinEncontrados = busquedas.buscar(siniestro, null, null);
        List<ResultadoBusqueda> resultados = busquedas.obtenerResultadosBusqueda(sinEncontrados);
        
        /* List<Siniestro> siniestros = siniestroService.findAllSiniestroes();        
        List<ResultadoBusqueda> resultados = busquedas.obtenerResultadosBusqueda(siniestros); */
       
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
