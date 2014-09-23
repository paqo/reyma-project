package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Provincia;
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

@RequestMapping("/provincias")
@Controller
public class ProvinciaController {

	@Autowired
    ProvinciaService provinciaService;

	@Autowired
    DomicilioService domicilioService;

	@Autowired
    MunicipioService municipioService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Provincia provincia, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, provincia);
            return "provincias/create";
        }
        uiModel.asMap().clear();
        provinciaService.saveProvincia(provincia);
        return "redirect:/provincias/" + encodeUrlPathSegment(provincia.getPrvId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Provincia());
        return "provincias/create";
    }

	@RequestMapping(value = "/{prvId}", produces = "text/html")
    public String show(@PathVariable("prvId") Integer prvId, Model uiModel) {
        uiModel.addAttribute("provincia", provinciaService.findProvincia(prvId));
        uiModel.addAttribute("itemId", prvId);
        return "provincias/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("provincias", Provincia.findProvinciaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) provinciaService.countAllProvincias() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("provincias", Provincia.findAllProvincias(sortFieldName, sortOrder));
        }
        return "provincias/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Provincia provincia, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, provincia);
            return "provincias/update";
        }
        uiModel.asMap().clear();
        provinciaService.updateProvincia(provincia);
        return "redirect:/provincias/" + encodeUrlPathSegment(provincia.getPrvId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{prvId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("prvId") Integer prvId, Model uiModel) {
        populateEditForm(uiModel, provinciaService.findProvincia(prvId));
        return "provincias/update";
    }

	@RequestMapping(value = "/{prvId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("prvId") Integer prvId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Provincia provincia = provinciaService.findProvincia(prvId);
        provinciaService.deleteProvincia(provincia);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/provincias";
    }

	void populateEditForm(Model uiModel, Provincia provincia) {
        uiModel.addAttribute("provincia", provincia);
        uiModel.addAttribute("domicilios", domicilioService.findAllDomicilios());
        uiModel.addAttribute("municipios", municipioService.findAllMunicipios());
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
