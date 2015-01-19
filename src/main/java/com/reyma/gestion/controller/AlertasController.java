package com.reyma.gestion.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.SiniestroService;

import flexjson.JSONSerializer;

@RequestMapping("/alertas")
@Controller
public class AlertasController {

	@Autowired
	SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String alertaCaducados(Model uiModel, HttpServletRequest request) {
                
        uiModel.asMap().clear();
        JSONSerializer serializer = new JSONSerializer();
        
        List<Siniestro> caducados = siniestroService.findSiniestrosCaducados(null);
        Integer numCaducados = 0;
        if ( caducados != null ){
        	numCaducados = caducados.size();
        }        
        
        Map<String, Integer> test = new HashMap<String, Integer>();
        test.put("caducados", numCaducados);
        
        return serializer.exclude("*.class").serialize(test);    
        
        
        
    }	
	
}
