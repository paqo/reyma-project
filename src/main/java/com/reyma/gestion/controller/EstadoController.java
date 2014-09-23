package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Estado;
import com.reyma.gestion.service.EstadoService;
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

@RequestMapping("/estadoes")
@Controller
public class EstadoController {

	@Autowired
    EstadoService estadoService;

	@Autowired
    SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Estado estado, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, estado);
            return "estadoes/create";
        }
        uiModel.asMap().clear();
        estadoService.saveEstado(estado);
        return "redirect:/estadoes/" + encodeUrlPathSegment(estado.getEstId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Estado());
        return "estadoes/create";
    }

	@RequestMapping(value = "/{estId}", produces = "text/html")
    public String show(@PathVariable("estId") Integer estId, Model uiModel) {
        uiModel.addAttribute("estado", estadoService.findEstado(estId));
        uiModel.addAttribute("itemId", estId);
        return "estadoes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("estadoes", Estado.findEstadoEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) estadoService.countAllEstadoes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("estadoes", Estado.findAllEstadoes(sortFieldName, sortOrder));
        }
        return "estadoes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Estado estado, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, estado);
            return "estadoes/update";
        }
        uiModel.asMap().clear();
        estadoService.updateEstado(estado);
        return "redirect:/estadoes/" + encodeUrlPathSegment(estado.getEstId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{estId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("estId") Integer estId, Model uiModel) {
        populateEditForm(uiModel, estadoService.findEstado(estId));
        return "estadoes/update";
    }

	@RequestMapping(value = "/{estId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("estId") Integer estId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Estado estado = estadoService.findEstado(estId);
        estadoService.deleteEstado(estado);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/estadoes";
    }

	void populateEditForm(Model uiModel, Estado estado) {
        uiModel.addAttribute("estado", estado);
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
