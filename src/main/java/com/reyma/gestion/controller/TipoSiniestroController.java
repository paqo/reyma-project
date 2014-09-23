package com.reyma.gestion.controller;
import com.reyma.gestion.dao.TipoSiniestro;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;

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

@RequestMapping("/tiposiniestroes")
@Controller
public class TipoSiniestroController {

	@Autowired
    TipoSiniestroService tipoSiniestroService;

	@Autowired
    SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid TipoSiniestro tipoSiniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoSiniestro);
            return "tiposiniestroes/create";
        }
        uiModel.asMap().clear();
        tipoSiniestroService.saveTipoSiniestro(tipoSiniestro);
        return "redirect:/tiposiniestroes/" + encodeUrlPathSegment(tipoSiniestro.getTsiId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new TipoSiniestro());
        return "tiposiniestroes/create";
    }

	@RequestMapping(value = "/{tsiId}", produces = "text/html")
    public String show(@PathVariable("tsiId") Integer tsiId, Model uiModel) {
        uiModel.addAttribute("tiposiniestro", tipoSiniestroService.findTipoSiniestro(tsiId));
        uiModel.addAttribute("itemId", tsiId);
        return "tiposiniestroes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("tiposiniestroes", TipoSiniestro.findTipoSiniestroEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) tipoSiniestroService.countAllTipoSiniestroes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("tiposiniestroes", TipoSiniestro.findAllTipoSiniestroes(sortFieldName, sortOrder));
        }
        return "tiposiniestroes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid TipoSiniestro tipoSiniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoSiniestro);
            return "tiposiniestroes/update";
        }
        uiModel.asMap().clear();
        tipoSiniestroService.updateTipoSiniestro(tipoSiniestro);
        return "redirect:/tiposiniestroes/" + encodeUrlPathSegment(tipoSiniestro.getTsiId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{tsiId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("tsiId") Integer tsiId, Model uiModel) {
        populateEditForm(uiModel, tipoSiniestroService.findTipoSiniestro(tsiId));
        return "tiposiniestroes/update";
    }

	@RequestMapping(value = "/{tsiId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("tsiId") Integer tsiId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TipoSiniestro tipoSiniestro = tipoSiniestroService.findTipoSiniestro(tsiId);
        tipoSiniestroService.deleteTipoSiniestro(tipoSiniestro);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/tiposiniestroes";
    }

	void populateEditForm(Model uiModel, TipoSiniestro tipoSiniestro) {
        uiModel.addAttribute("tipoSiniestro", tipoSiniestro);
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
