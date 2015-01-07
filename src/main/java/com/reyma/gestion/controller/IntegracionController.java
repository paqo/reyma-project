package com.reyma.gestion.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.integracion.IntegracionHelper;
import com.reyma.gestion.integracion.MensajeResultadoIntegracion;

import flexjson.JSONSerializer;

@RequestMapping("/integracion")
@Controller
public class IntegracionController {

	@Autowired
	IntegracionHelper integracion;
		
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String importar(Siniestro siniestro, Domicilio domicilio, Persona asegurado, HttpServletRequest request) {
			
		MensajeResultadoIntegracion resultado = integracion.procesarPeticion(siniestro, domicilio, asegurado, request);		
		JSONSerializer serializer = new JSONSerializer();
		
        return serializer.exclude("*.class").serialize(resultado);        
    }	
	
}
