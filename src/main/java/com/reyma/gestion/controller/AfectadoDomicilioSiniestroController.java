package com.reyma.gestion.controller;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoAfectacionService;

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

@RequestMapping("/afectadodomiciliosiniestroes")
@Controller
public class AfectadoDomicilioSiniestroController {

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@Autowired
    DomicilioService domicilioService;

	@Autowired
    PersonaService personaService;

	@Autowired
    SiniestroService siniestroService;

	@Autowired
    TipoAfectacionService tipoAfectacionService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AfectadoDomicilioSiniestro afectadoDomicilioSiniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, afectadoDomicilioSiniestro);
            return "afectadodomiciliosiniestroes/create";
        }
        uiModel.asMap().clear();
        afectadoDomicilioSiniestroService.saveAfectadoDomicilioSiniestro(afectadoDomicilioSiniestro);
        return "redirect:/afectadodomiciliosiniestroes/" + encodeUrlPathSegment(afectadoDomicilioSiniestro.getAdsId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AfectadoDomicilioSiniestro());
        return "afectadodomiciliosiniestroes/create";
    }

	@RequestMapping(value = "/{adsId}", produces = "text/html")
    public String show(@PathVariable("adsId") Integer adsId, Model uiModel) {
        uiModel.addAttribute("afectadodomiciliosiniestro", afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(adsId));
        uiModel.addAttribute("itemId", adsId);
        return "afectadodomiciliosiniestroes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("afectadodomiciliosiniestroes", AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestroEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) afectadoDomicilioSiniestroService.countAllAfectadoDomicilioSiniestroes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("afectadodomiciliosiniestroes", AfectadoDomicilioSiniestro.findAllAfectadoDomicilioSiniestroes(sortFieldName, sortOrder));
        }
        return "afectadodomiciliosiniestroes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AfectadoDomicilioSiniestro afectadoDomicilioSiniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, afectadoDomicilioSiniestro);
            return "afectadodomiciliosiniestroes/update";
        }
        uiModel.asMap().clear();
        afectadoDomicilioSiniestroService.updateAfectadoDomicilioSiniestro(afectadoDomicilioSiniestro);
        return "redirect:/afectadodomiciliosiniestroes/" + encodeUrlPathSegment(afectadoDomicilioSiniestro.getAdsId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{adsId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("adsId") Integer adsId, Model uiModel) {
        populateEditForm(uiModel, afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(adsId));
        return "afectadodomiciliosiniestroes/update";
    }

	@RequestMapping(value = "/{adsId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("adsId") Integer adsId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AfectadoDomicilioSiniestro afectadoDomicilioSiniestro = afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(adsId);
        afectadoDomicilioSiniestroService.deleteAfectadoDomicilioSiniestro(afectadoDomicilioSiniestro);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/afectadodomiciliosiniestroes";
    }

	void populateEditForm(Model uiModel, AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
        uiModel.addAttribute("afectadoDomicilioSiniestro", afectadoDomicilioSiniestro);
        uiModel.addAttribute("domicilios", domicilioService.findAllDomicilios());
        uiModel.addAttribute("personae", personaService.findAllPersonae());
        uiModel.addAttribute("siniestroes", siniestroService.findAllSiniestroes());
        uiModel.addAttribute("tipoafectacions", tipoAfectacionService.findAllTipoAfectacions());
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
