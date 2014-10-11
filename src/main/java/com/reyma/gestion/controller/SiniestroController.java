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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.EstadoService;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.util.Fechas;

@RequestMapping("/siniestroes")
@Controller
public class SiniestroController {

	@Autowired
    SiniestroService siniestroService;

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@Autowired
    CompaniaService companiaService;

	@Autowired
    EstadoService estadoService;

	@Autowired
    FacturaService facturaService;

	@Autowired
    TipoSiniestroService tipoSiniestroService;

	@Autowired
    TrabajoService trabajoService;
	
	@Autowired
    ProvinciaService provinciaService;
	
	@Autowired
    MunicipioService municipioService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siniestro);
            return "siniestroes/create";
        }
        uiModel.asMap().clear();
        siniestroService.saveSiniestro(siniestro);
        //return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest);
        return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest) + "?form";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Siniestro());
        return "siniestroes/create";
    }

	@RequestMapping(value = "/{sinId}", produces = "text/html")
    public String show(@PathVariable("sinId") Integer sinId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("siniestro", siniestroService.findSiniestro(sinId));
        uiModel.addAttribute("itemId", sinId);
        return "siniestroes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("siniestroes", Siniestro.findSiniestroEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) siniestroService.countAllSiniestroes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("siniestroes", Siniestro.findAllSiniestroes(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "siniestroes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, siniestro);
            return "siniestroes/update";
        }
        uiModel.asMap().clear();
        siniestroService.updateSiniestro(siniestro);
        return "redirect:/siniestroes/" + encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{sinId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("sinId") Integer sinId, Model uiModel) {
        populateEditForm(uiModel, siniestroService.findSiniestro(sinId));
        return "siniestroes/update";
    }

	@RequestMapping(value = "/{sinId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("sinId") Integer sinId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Siniestro siniestro = siniestroService.findSiniestro(sinId);
        siniestroService.deleteSiniestro(siniestro);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "5" : size.toString());
        return "redirect:/siniestroes";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        /*uiModel.addAttribute("siniestro_sinfechacomunicacion_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("siniestro_sinfechaocurrencia_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));*/
		uiModel.addAttribute("siniestro_sinfechacomunicacion_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
        uiModel.addAttribute("siniestro_sinfechaocurrencia_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
    }

	void populateEditForm(Model uiModel, Siniestro siniestro) {
        uiModel.addAttribute("siniestro", siniestro);
        addDateTimeFormatPatterns(uiModel);
        //TODO: ver como se hace esto "bien" (acceso a elemento de lista
        // en 'path')
        List<AfectadoDomicilioSiniestro> listaAfectados = afectadoDomicilioSiniestroService.findAfectadosDomicilioBySiniestro(siniestro.getSinId());
        int cont = 1;
        for (AfectadoDomicilioSiniestro ads : listaAfectados) {
        	 uiModel.addAttribute("domicilio-" + cont++, ads.getAdsDomId());
		}
        uiModel.addAttribute("afectadodomiciliosiniestroes", listaAfectados);
        //TODO: facturas y trabajos solamente del siniestro
        uiModel.addAttribute("facturas", facturaService.findAllFacturas());         
        uiModel.addAttribute("trabajoes", trabajoService.findAllTrabajoes());
        // desplegables
        uiModel.addAttribute("companias", companiaService.findAllCompanias());
        uiModel.addAttribute("estadoes", estadoService.findAllEstadoes());
        uiModel.addAttribute("tiposiniestroes", tipoSiniestroService.findAllTipoSiniestroes());
        uiModel.addAttribute("provincias", provinciaService.findAllProvincias());
        uiModel.addAttribute("municipios", municipioService.findAllMunicipiosByIdProvincia(41));
        // ponemos por defecto sevilla como provincia y municipio        
        Domicilio domicilio = new Domicilio();
        //TODO: cargar id de sevilla desde base de datos
        Municipio mun = municipioService.findMunicipio(5);
        domicilio.setDomMunId(mun);
        domicilio.setDomProvId(mun.getMunPrvId());        
        uiModel.addAttribute("domicilio", domicilio);
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
