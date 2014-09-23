package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Compania;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.SiniestroService;

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

@RequestMapping("/companias")
@Controller
public class CompaniaController {

	@Autowired
    CompaniaService companiaService;

	@Autowired
    SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Compania compania, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compania);
            return "companias/create";
        }
        uiModel.asMap().clear();
        companiaService.saveCompania(compania);
        return "redirect:/companias/" + encodeUrlPathSegment(compania.getComId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Compania());
        return "companias/create";
    }

	@RequestMapping(value = "/{comId}", produces = "text/html")
    public String show(@PathVariable("comId") Integer comId, Model uiModel) {
        uiModel.addAttribute("compania", companiaService.findCompania(comId));
        uiModel.addAttribute("itemId", comId);
        return "companias/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("companias", Compania.findCompaniaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) companiaService.countAllCompanias() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("companias", Compania.findAllCompanias(sortFieldName, sortOrder));
        }
        return "companias/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Compania compania, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compania);
            return "companias/update";
        }
        uiModel.asMap().clear();
        companiaService.updateCompania(compania);
        return "redirect:/companias/" + encodeUrlPathSegment(compania.getComId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{comId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("comId") Integer comId, Model uiModel) {
        populateEditForm(uiModel, companiaService.findCompania(comId));
        return "companias/update";
    }

	@RequestMapping(value = "/{comId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("comId") Integer comId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Compania compania = companiaService.findCompania(comId);
        companiaService.deleteCompania(compania);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/companias";
    }

	void populateEditForm(Model uiModel, Compania compania) {
        uiModel.addAttribute("compania", compania);
        uiModel.addAttribute("siniestroes", siniestroService.findAllSiniestroes());
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
