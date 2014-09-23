package com.reyma.gestion.controller;
import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
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

@RequestMapping("/tipoafectacions")
@Controller
public class TipoAfectacionController {

	@Autowired
    TipoAfectacionService tipoAfectacionService;

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid TipoAfectacion tipoAfectacion, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoAfectacion);
            return "tipoafectacions/create";
        }
        uiModel.asMap().clear();
        tipoAfectacionService.saveTipoAfectacion(tipoAfectacion);
        return "redirect:/tipoafectacions/" + encodeUrlPathSegment(tipoAfectacion.getTafId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new TipoAfectacion());
        return "tipoafectacions/create";
    }

	@RequestMapping(value = "/{tafId}", produces = "text/html")
    public String show(@PathVariable("tafId") Integer tafId, Model uiModel) {
        uiModel.addAttribute("tipoafectacion", tipoAfectacionService.findTipoAfectacion(tafId));
        uiModel.addAttribute("itemId", tafId);
        return "tipoafectacions/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("tipoafectacions", TipoAfectacion.findTipoAfectacionEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) tipoAfectacionService.countAllTipoAfectacions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("tipoafectacions", TipoAfectacion.findAllTipoAfectacions(sortFieldName, sortOrder));
        }
        return "tipoafectacions/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid TipoAfectacion tipoAfectacion, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoAfectacion);
            return "tipoafectacions/update";
        }
        uiModel.asMap().clear();
        tipoAfectacionService.updateTipoAfectacion(tipoAfectacion);
        return "redirect:/tipoafectacions/" + encodeUrlPathSegment(tipoAfectacion.getTafId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{tafId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("tafId") Integer tafId, Model uiModel) {
        populateEditForm(uiModel, tipoAfectacionService.findTipoAfectacion(tafId));
        return "tipoafectacions/update";
    }

	@RequestMapping(value = "/{tafId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("tafId") Integer tafId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TipoAfectacion tipoAfectacion = tipoAfectacionService.findTipoAfectacion(tafId);
        tipoAfectacionService.deleteTipoAfectacion(tipoAfectacion);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/tipoafectacions";
    }

	void populateEditForm(Model uiModel, TipoAfectacion tipoAfectacion) {
        uiModel.addAttribute("tipoAfectacion", tipoAfectacion);
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
