package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.ProvinciaService;

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

@RequestMapping("/domicilios")
@Controller
public class DomicilioController {

	@Autowired
    DomicilioService domicilioService;

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@Autowired
    MunicipioService municipioService;

	@Autowired
    ProvinciaService provinciaService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Domicilio domicilio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, domicilio);
            return "domicilios/create";
        }
        uiModel.asMap().clear();
        domicilioService.saveDomicilio(domicilio);
        return "redirect:/domicilios/" + encodeUrlPathSegment(domicilio.getDomId().toString(), httpServletRequest) + "?form";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Domicilio());
        return "domicilios/create";
    }

	@RequestMapping(value = "/{domId}", produces = "text/html")
    public String show(@PathVariable("domId") Integer domId, Model uiModel) {
        uiModel.addAttribute("domicilio", domicilioService.findDomicilio(domId));
        uiModel.addAttribute("itemId", domId);
        return "domicilios/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("domicilios", Domicilio.findDomicilioEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) domicilioService.countAllDomicilios() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("domicilios", Domicilio.findAllDomicilios(sortFieldName, sortOrder));
        }
        return "domicilios/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Domicilio domicilio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, domicilio);
            return "domicilios/update";
        }
        uiModel.asMap().clear();
        domicilioService.updateDomicilio(domicilio);
        return "redirect:/domicilios/" + encodeUrlPathSegment(domicilio.getDomId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{domId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("domId") Integer domId, Model uiModel) {
        populateEditForm(uiModel, domicilioService.findDomicilio(domId));
        return "domicilios/update";
    }

	@RequestMapping(value = "/{domId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("domId") Integer domId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Domicilio domicilio = domicilioService.findDomicilio(domId);
        domicilioService.deleteDomicilio(domicilio);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/domicilios";
    }

	void populateEditForm(Model uiModel, Domicilio domicilio) {
        uiModel.addAttribute("domicilio", domicilio);
        uiModel.addAttribute("afectadodomiciliosiniestroes", afectadoDomicilioSiniestroService.findAllAfectadoDomicilioSiniestroes());
        uiModel.addAttribute("municipios", municipioService.findAllMunicipios());
        uiModel.addAttribute("provincias", provinciaService.findAllProvincias());
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
