package com.reyma.gestion.controller;
import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.service.FacturaService;
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

@RequestMapping("/lineafacturas")
@Controller
public class LineaFacturaController {

	@Autowired
    LineaFacturaService lineaFacturaService;

	@Autowired
    FacturaService facturaService;

	@Autowired
    IvaService ivaService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LineaFactura lineaFactura, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lineaFactura);
            return "lineafacturas/create";
        }
        uiModel.asMap().clear();
        lineaFacturaService.saveLineaFactura(lineaFactura);
        return "redirect:/lineafacturas/" + encodeUrlPathSegment(lineaFactura.getLinId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LineaFactura());
        return "lineafacturas/create";
    }

	@RequestMapping(value = "/{linId}", produces = "text/html")
    public String show(@PathVariable("linId") Integer linId, Model uiModel) {
        uiModel.addAttribute("lineafactura", lineaFacturaService.findLineaFactura(linId));
        uiModel.addAttribute("itemId", linId);
        return "lineafacturas/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lineafacturas", LineaFactura.findLineaFacturaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) lineaFacturaService.countAllLineaFacturas() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lineafacturas", LineaFactura.findAllLineaFacturas(sortFieldName, sortOrder));
        }
        return "lineafacturas/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LineaFactura lineaFactura, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lineaFactura);
            return "lineafacturas/update";
        }
        uiModel.asMap().clear();
        lineaFacturaService.updateLineaFactura(lineaFactura);
        return "redirect:/lineafacturas/" + encodeUrlPathSegment(lineaFactura.getLinId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{linId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("linId") Integer linId, Model uiModel) {
        populateEditForm(uiModel, lineaFacturaService.findLineaFactura(linId));
        return "lineafacturas/update";
    }

	@RequestMapping(value = "/{linId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("linId") Integer linId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LineaFactura lineaFactura = lineaFacturaService.findLineaFactura(linId);
        lineaFacturaService.deleteLineaFactura(lineaFactura);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lineafacturas";
    }

	void populateEditForm(Model uiModel, LineaFactura lineaFactura) {
        uiModel.addAttribute("lineaFactura", lineaFactura);
        uiModel.addAttribute("facturas", facturaService.findAllFacturas());
        uiModel.addAttribute("ivas", ivaService.findAllIvas());
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
