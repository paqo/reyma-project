package com.reyma.gestion.controller;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Trabajo;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.OperarioService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.ui.MensajeDialogoUIBase;
import com.reyma.gestion.ui.MensajeErrorValidacionJson;
import com.reyma.gestion.ui.MensajeExitoJson;

import flexjson.JSONSerializer;

@RequestMapping("/trabajos")
@Controller
public class TrabajoController {

	@Autowired
    TrabajoService trabajoService;

	@Autowired
    OficioService oficioService;

	@Autowired
    OperarioService operarioService;

	@Autowired
    SiniestroService siniestroService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Trabajo trabajo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, trabajo);
            return "trabajos/create";
        }
        uiModel.asMap().clear();
        trabajoService.saveTrabajo(trabajo);
        return "redirect:/trabajos/" + encodeUrlPathSegment(trabajo.getTraId().toString(), httpServletRequest);
    }
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String alta(@Valid Trabajo trabajo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {        
		JSONSerializer serializer = new JSONSerializer();
		//MensajeErrorValidacionJson mensajeError = null;
		//TODO: VALIDACIONES
		trabajoService.saveTrabajo(trabajo);
		MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos del trabajo se han guardado con éxito", true);
		return serializer.exclude("class").serialize(mensajeExito);
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String actualizar(@Valid Trabajo trabajo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {        
		JSONSerializer serializer = new JSONSerializer();		
		trabajoService.updateTrabajo(trabajo);
		MensajeExitoJson mensajeExito = new MensajeExitoJson("Los datos del trabajo se han actualizado con éxito", true);
		return serializer.exclude("class").serialize(mensajeExito);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Trabajo());
        return "trabajos/create";
    }

	@RequestMapping(value = "/{traId}", produces = "text/html")
    public String show(@PathVariable("traId") Integer traId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("trabajo", trabajoService.findTrabajo(traId));
        uiModel.addAttribute("itemId", traId);
        return "trabajos/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("trabajos", Trabajo.findTrabajoEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) trabajoService.countAllTrabajoes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("trabajos", Trabajo.findAllTrabajoes(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "trabajos/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Trabajo trabajo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, trabajo);
            return "trabajos/update";
        }
        uiModel.asMap().clear();
        trabajoService.updateTrabajo(trabajo);
        return "redirect:/trabajos/" + encodeUrlPathSegment(trabajo.getTraId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{traId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("traId") Integer traId, Model uiModel) {
        populateEditForm(uiModel, trabajoService.findTrabajo(traId));
        return "trabajos/update";
    }
	
	@RequestMapping(value = "/remove/{traId}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String delete(@PathVariable("traId") Integer traId, Model uiModel) {
		MensajeDialogoUIBase mensaje;
		JSONSerializer serializer = new JSONSerializer();
		Trabajo trabajo = trabajoService.findTrabajo(traId);		
		if ( trabajoService.deleteTrabajo(trabajo) ){
			uiModel.asMap().clear();
        	mensaje = new MensajeExitoJson("Datos eliminados con éxito", true);
		}else {
			mensaje = new MensajeErrorValidacionJson("Error eliminando datos del trabajo");
		}
        return serializer.exclude("class").serialize(mensaje);
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("trabajo_trafecha_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Trabajo trabajo) {
        uiModel.addAttribute("trabajo", trabajo);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("oficios", oficioService.findAllOficios());
        uiModel.addAttribute("operarios", operarioService.findAllOperarios());
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
