package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.controller.validators.AfectadoValidator;
import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoAfectacionService;

import flexjson.JSONSerializer;

@RequestMapping("/afectados")
@Controller
public class AfectadosController {
	
	@Autowired
    SiniestroService siniestroService;

	@Autowired
    PersonaService personaService;
	
	@Autowired
    DomicilioService domicilioService;
	
	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;	
	
	@Autowired
    TipoAfectacionService tipoAfectacionService;	

	@InitBinder
    protected void initBinder(WebDataBinder binder) {        
        binder.addValidators(new AfectadoValidator());
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String alta(@Valid Persona persona, BindingResult bindingResultP, 
			@Valid Domicilio domicilio, BindingResult bindingResultD, Model uiModel) {		
		List<FieldError> errores = new ArrayList<FieldError>();		
		// lista con todos los errores de validacion de afectados
		errores = UtilsValidacion.getErroresValidacion(bindingResultP.getFieldErrors());		
		errores.addAll(UtilsValidacion.getErroresValidacion(bindingResultD.getFieldErrors()));
		
		for (FieldError fieldError : errores) {
			System.out.println("=> " + fieldError.getField() + ": " + fieldError.getDefaultMessage());
		}
		
		JSONSerializer serializer = new JSONSerializer();
		return serializer.exclude("class").serialize(domicilio); // class siempre lo lleva, hay que excluir 
    }	
	
	void populateEditForm(Model uiModel, Siniestro siniestro) {        
        uiModel.addAttribute("tiposAfectacion", tipoAfectacionService.findAllTipoAfectacions());
        
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String createee(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		//AfectadoDomicilioSiniestro ads;
				
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siniestro);
            return "siniestroes/create";
        }
        uiModel.asMap().clear();
        siniestroService.saveSiniestro(siniestro);
        //return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest);
        return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest) + "?form";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Siniestro());
        return "siniestroes/create";
    }

	@RequestMapping(value = "/{sinId}", produces = "text/html")
    public String show(@PathVariable("sinId") Integer sinId, Model uiModel) {
        uiModel.addAttribute("siniestro", siniestroService.findSiniestro(sinId));
        uiModel.addAttribute("itemId", sinId);
        return "siniestroes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("siniestroes", Siniestro.findSiniestroEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) siniestroService.countAllSiniestroes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("siniestroes", Siniestro.findAllSiniestroes(sortFieldName, sortOrder));
        }
        return "siniestroes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siniestro);
            return "siniestroes/update";
        }
        uiModel.asMap().clear();
        siniestroService.updateSiniestro(siniestro);
        return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{sinId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("sinId") Integer sinId, Model uiModel) {
        populateEditForm(uiModel, siniestroService.findSiniestro(sinId));
        return "siniestroes/update";
    }

	@RequestMapping(value = "/{sinId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("sinId") Integer sinId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Siniestro siniestro = siniestroService.findSiniestro(sinId);
        siniestroService.deleteSiniestro(siniestro);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "5" : size.toString());
        return "redirect:/siniestroes";
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
