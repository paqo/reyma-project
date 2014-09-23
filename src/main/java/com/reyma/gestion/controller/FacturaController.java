package com.reyma.gestion.controller;
import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.LineaFacturaService;
import com.reyma.gestion.service.SiniestroService;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/facturas")
@Controller
public class FacturaController {

	@Autowired
    FacturaService facturaService;

	@Autowired
    LineaFacturaService lineaFacturaService;

	@Autowired
    SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Factura factura, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, factura);
            return "facturas/create";
        }
        uiModel.asMap().clear();
        facturaService.saveFactura(factura);
        return "redirect:/facturas/" + encodeUrlPathSegment(factura.getFacId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Factura());
        return "facturas/create";
    }

	@RequestMapping(value = "/{facId}", produces = "text/html")
    public String show(@PathVariable("facId") Integer facId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("factura", facturaService.findFactura(facId));
        uiModel.addAttribute("itemId", facId);
        return "facturas/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("facturas", Factura.findFacturaEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) facturaService.countAllFacturas() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("facturas", Factura.findAllFacturas(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "facturas/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Factura factura, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, factura);
            return "facturas/update";
        }
        uiModel.asMap().clear();
        facturaService.updateFactura(factura);
        return "redirect:/facturas/" + encodeUrlPathSegment(factura.getFacId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{facId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("facId") Integer facId, Model uiModel) {
        populateEditForm(uiModel, facturaService.findFactura(facId));
        return "facturas/update";
    }

	@RequestMapping(value = "/{facId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("facId") Integer facId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Factura factura = facturaService.findFactura(facId);
        facturaService.deleteFactura(factura);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/facturas";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("factura_facfecha_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Factura factura) {
        uiModel.addAttribute("factura", factura);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lineafacturas", lineaFacturaService.findAllLineaFacturas());
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
