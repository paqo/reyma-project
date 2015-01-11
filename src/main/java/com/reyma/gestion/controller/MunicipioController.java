package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.ui.AutocompleteJSONBean;

import flexjson.JSONSerializer;

@RequestMapping("/municipios")
@Controller
public class MunicipioController {

	@Autowired
    MunicipioService municipioService;

	@Autowired
    DomicilioService domicilioService;

	@Autowired
    ProvinciaService provinciaService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Municipio municipio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, municipio);
            return "municipios/create";
        }
        uiModel.asMap().clear();
        municipioService.saveMunicipio(municipio);
        return "redirect:/municipios/" + encodeUrlPathSegment(municipio.getMunId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Municipio());
        return "municipios/create";
    }

	@RequestMapping(value = "/{munId}", produces = "text/html")
    public String show(@PathVariable("munId") Integer munId, Model uiModel) {
        uiModel.addAttribute("municipio", municipioService.findMunicipio(munId));
        uiModel.addAttribute("itemId", munId);
        return "municipios/show";
    }
	
	
	@RequestMapping(value = "/getJson", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String obtenerMunicipiosByIdProvYDescAjax(Municipio municipio, Model uiModel) {
		JSONSerializer serializer = new JSONSerializer();
		System.out.println("=> municipio: " + municipio.getMunDescripcion() +
				"=> idMun: " + municipio.getMunId()+
				", idProv: " + municipio.getMunPrvId());
		List<AutocompleteJSONBean> municipios = municipioService.
				findMunicipiosParaAutocomplete(municipio.getMunPrvId().getPrvId(), municipio.getMunDescripcion());
		return serializer.exclude("*.class").serialize(municipios);
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("municipios", Municipio.findMunicipioEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) municipioService.countAllMunicipios() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("municipios", Municipio.findAllMunicipios(sortFieldName, sortOrder));
        }
        return "municipios/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Municipio municipio, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, municipio);
            return "municipios/update";
        }
        uiModel.asMap().clear();
        municipioService.updateMunicipio(municipio);
        return "redirect:/municipios/" + encodeUrlPathSegment(municipio.getMunId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{munId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("munId") Integer munId, Model uiModel) {
        populateEditForm(uiModel, municipioService.findMunicipio(munId));
        return "municipios/update";
    }

	@RequestMapping(value = "/{munId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("munId") Integer munId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Municipio municipio = municipioService.findMunicipio(munId);
        municipioService.deleteMunicipio(municipio);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/municipios";
    }

	void populateEditForm(Model uiModel, Municipio municipio) {
        uiModel.addAttribute("municipio", municipio);
        uiModel.addAttribute("domicilios", domicilioService.findAllDomicilios());
        uiModel.addAttribute("provincias", provinciaService.findAllProvincias());
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
