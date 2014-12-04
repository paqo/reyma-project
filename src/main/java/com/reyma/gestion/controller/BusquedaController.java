package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.EstadoService;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.OperarioService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.util.Fechas;

import flexjson.JSONSerializer;

@RequestMapping("/busquedas")
@Controller
public class BusquedaController {

	@Autowired
    SiniestroService siniestroService;

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@Autowired
    CompaniaService companiaService;

	@Autowired
    EstadoService estadoService;
	
	@Autowired
    OficioService oficioService;
	
	@Autowired
	OperarioService operarioService;

	@Autowired
    FacturaService facturaService;

	@Autowired
    TipoSiniestroService tipoSiniestroService;

	@Autowired
    TrabajoService trabajoService;
	
	@Autowired
    ProvinciaService provinciaService;
	
	@Autowired
    MunicipioService municipioService;
	
	@Autowired
    AfectadoDomicilioSiniestroService adsService;
	
	

	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public String buscar(Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	System.out.println("=> tiene errores.");      
        }
        
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
        Map<String, String> test = new HashMap<String, String>();
        test.put("test", "ola k a�e");
        return serializer.exclude("class").serialize(test);
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
