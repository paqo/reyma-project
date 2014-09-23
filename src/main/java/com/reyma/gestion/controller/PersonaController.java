package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.PersonaService;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/personae")
@Controller
public class PersonaController {

	@Autowired
    PersonaService personaService;

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Persona persona, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, persona);
            return "personae/create";
        }
        uiModel.asMap().clear();
        personaService.savePersona(persona);
        return "redirect:/personae/" + encodeUrlPathSegment(persona.getPerId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Persona());
        return "personae/create";
    }

	@RequestMapping(value = "/{perId}", produces = "text/html")
    public String show(@PathVariable("perId") Integer perId, Model uiModel) {
        uiModel.addAttribute("persona", personaService.findPersona(perId));
        uiModel.addAttribute("itemId", perId);
        return "personae/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("personae", Persona.findPersonaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) personaService.countAllPersonae() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("personae", Persona.findAllPersonae(sortFieldName, sortOrder));
        }
        return "personae/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Persona persona, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, persona);
            return "personae/update";
        }
        uiModel.asMap().clear();
        personaService.updatePersona(persona);
        return "redirect:/personae/" + encodeUrlPathSegment(persona.getPerId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{perId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("perId") Integer perId, Model uiModel) {
        populateEditForm(uiModel, personaService.findPersona(perId));
        return "personae/update";
    }

	@RequestMapping(value = "/{perId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("perId") Integer perId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Persona persona = personaService.findPersona(perId);
        personaService.deletePersona(persona);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/personae";
    }

	void populateEditForm(Model uiModel, Persona persona) {
        uiModel.addAttribute("persona", persona);
        uiModel.addAttribute("afectadodomiciliosiniestroes", afectadoDomicilioSiniestroService.findAllAfectadoDomicilioSiniestroes());
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
