package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Operario;
import com.reyma.gestion.service.OperarioService;
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

@RequestMapping("/operarios")
@Controller
public class OperarioController {

	@Autowired
    OperarioService operarioService;

	@Autowired
    TrabajoService trabajoService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Operario operario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, operario);
            return "operarios/create";
        }
        uiModel.asMap().clear();
        operarioService.saveOperario(operario);
        return "redirect:/operarios/" + encodeUrlPathSegment(operario.getOpeId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Operario());
        return "operarios/create";
    }

	@RequestMapping(value = "/{opeId}", produces = "text/html")
    public String show(@PathVariable("opeId") Integer opeId, Model uiModel) {
        uiModel.addAttribute("operario", operarioService.findOperario(opeId));
        uiModel.addAttribute("itemId", opeId);
        return "operarios/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operarios", Operario.findOperarioEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) operarioService.countAllOperarios() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operarios", Operario.findAllOperarios(sortFieldName, sortOrder));
        }
        return "operarios/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Operario operario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, operario);
            return "operarios/update";
        }
        uiModel.asMap().clear();
        operarioService.updateOperario(operario);
        return "redirect:/operarios/" + encodeUrlPathSegment(operario.getOpeId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{opeId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("opeId") Integer opeId, Model uiModel) {
        populateEditForm(uiModel, operarioService.findOperario(opeId));
        return "operarios/update";
    }

	@RequestMapping(value = "/{opeId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("opeId") Integer opeId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Operario operario = operarioService.findOperario(opeId);
        operarioService.deleteOperario(operario);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/operarios";
    }

	void populateEditForm(Model uiModel, Operario operario) {
        uiModel.addAttribute("operario", operario);
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
