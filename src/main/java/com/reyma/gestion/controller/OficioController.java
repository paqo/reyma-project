package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Oficio;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.TrabajoService;

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

@RequestMapping("/oficios")
@Controller
public class OficioController {

	@Autowired
    OficioService oficioService;

	@Autowired
    TrabajoService trabajoService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Oficio oficio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, oficio);
            return "oficios/create";
        }
        uiModel.asMap().clear();
        oficioService.saveOficio(oficio);
        return "redirect:/oficios/" + encodeUrlPathSegment(oficio.getOfiId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Oficio());
        return "oficios/create";
    }

	@RequestMapping(value = "/{ofiId}", produces = "text/html")
    public String show(@PathVariable("ofiId") Integer ofiId, Model uiModel) {
        uiModel.addAttribute("oficio", oficioService.findOficio(ofiId));
        uiModel.addAttribute("itemId", ofiId);
        return "oficios/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("oficios", Oficio.findOficioEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) oficioService.countAllOficios() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("oficios", Oficio.findAllOficios(sortFieldName, sortOrder));
        }
        return "oficios/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Oficio oficio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, oficio);
            return "oficios/update";
        }
        uiModel.asMap().clear();
        oficioService.updateOficio(oficio);
        return "redirect:/oficios/" + encodeUrlPathSegment(oficio.getOfiId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{ofiId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("ofiId") Integer ofiId, Model uiModel) {
        populateEditForm(uiModel, oficioService.findOficio(ofiId));
        return "oficios/update";
    }

	@RequestMapping(value = "/{ofiId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("ofiId") Integer ofiId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Oficio oficio = oficioService.findOficio(ofiId);
        oficioService.deleteOficio(oficio);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/oficios";
    }

	void populateEditForm(Model uiModel, Oficio oficio) {
        uiModel.addAttribute("oficio", oficio);
        uiModel.addAttribute("trabajoes", trabajoService.findAllTrabajoes());
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
