package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.service.IvaService;
import com.reyma.gestion.service.LineaFacturaService;

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

@RequestMapping("/ivas")
@Controller
public class IvaController {

	@Autowired
    IvaService ivaService;

	@Autowired
    LineaFacturaService lineaFacturaService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Iva iva, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iva);
            return "ivas/create";
        }
        uiModel.asMap().clear();
        ivaService.saveIva(iva);
        return "redirect:/ivas/" + encodeUrlPathSegment(iva.getIvaId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Iva());
        return "ivas/create";
    }

	@RequestMapping(value = "/{ivaId}", produces = "text/html")
    public String show(@PathVariable("ivaId") Integer ivaId, Model uiModel) {
        uiModel.addAttribute("iva", ivaService.findIva(ivaId));
        uiModel.addAttribute("itemId", ivaId);
        return "ivas/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ivas", Iva.findIvaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ivaService.countAllIvas() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ivas", Iva.findAllIvas(sortFieldName, sortOrder));
        }
        return "ivas/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Iva iva, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iva);
            return "ivas/update";
        }
        uiModel.asMap().clear();
        ivaService.updateIva(iva);
        return "redirect:/ivas/" + encodeUrlPathSegment(iva.getIvaId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{ivaId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("ivaId") Integer ivaId, Model uiModel) {
        populateEditForm(uiModel, ivaService.findIva(ivaId));
        return "ivas/update";
    }

	@RequestMapping(value = "/{ivaId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("ivaId") Integer ivaId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Iva iva = ivaService.findIva(ivaId);
        ivaService.deleteIva(iva);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ivas";
    }

	void populateEditForm(Model uiModel, Iva iva) {
        uiModel.addAttribute("iva", iva);
        uiModel.addAttribute("lineafacturas", lineaFacturaService.findAllLineaFacturas());
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
