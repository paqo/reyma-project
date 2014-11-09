package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.Trabajo;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.EstadoService;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.OperarioService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.ui.listados.SiniestroListadoDTO;
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
    OficioService oficioService;
	
	@Autowired
	OperarioService operarioService;

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
	
	@Autowired
    AfectadoDomicilioSiniestroService adsService;
	
	

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
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            List<Siniestro> siniestros = siniestroService.findSiniestroEntries(firstResult, sizeNo, "sinFechaOcurrencia", "DESC");
            List<SiniestroListadoDTO> listaSiniestros = new ArrayList<SiniestroListadoDTO>();
            SiniestroListadoDTO dto;
            for (Siniestro siniestro : siniestros) {
				dto = new SiniestroListadoDTO(siniestro, afectadoDomicilioSiniestroService);
				listaSiniestros.add(dto);
			}            
            uiModel.addAttribute("siniestroes", listaSiniestros);
            float nrOfPages = (float) siniestroService.countAllSiniestroes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        	List<Siniestro> siniestros = siniestroService.findAllSiniestroes("sinFechaOcurrencia", "DESC");
            List<SiniestroListadoDTO> listaSiniestros = new ArrayList<SiniestroListadoDTO>();
            SiniestroListadoDTO dto;
            for (Siniestro siniestro : siniestros) {
				dto = new SiniestroListadoDTO(siniestro, afectadoDomicilioSiniestroService);
				listaSiniestros.add(dto);
			} 
            uiModel.addAttribute("siniestroes", listaSiniestros);
        }
        return "siniestroes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		if (bindingResult.hasErrors()) {			
			if ( UtilsValidacion.ignorarErrorBooleanBinding(bindingResult) ){
	        	// no es realmente error, valor "on" para el checkbox
				// TODO: modificar la manera en que se hace el binding
				// (no se pueden usar converters de jpa por la version usada)
				siniestro.setSinUrgente((short)1);
			} else {
		    	populateEditForm(uiModel, siniestro);
	            return "siniestroes/update";
		    }            
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
        
        List<AfectadoDomicilioSiniestro> afectados = 
        		afectadoDomicilioSiniestroService.findAfectadosDomicilioBySiniestro(siniestro.getSinId());
        
        List<Trabajo> trabajos = trabajoService.findTrabajosByIdSiniestro(siniestro.getSinId());
        
        // afectados
        uiModel.addAttribute("afectadodomiciliosiniestroes", afectados);
        // trabajos
        uiModel.addAttribute("trabajos", trabajos);
        //TODO: facturas solamente del siniestro
        uiModel.addAttribute("facturas", facturaService.findAllFacturas());         
        
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
        uiModel.addAttribute("municipios", municipioService.findAllMunicipiosByIdProvincia(41));
        
        uiModel.addAttribute("trabajo-1", new Trabajo());
        uiModel.addAttribute("oficios", oficioService.findAllOficios() );
        uiModel.addAttribute("operarios", operarioService.findAllOperarios() );
        
        fixPathDesplegables(uiModel, siniestro, afectados, trabajos);
        
        
    }

	private void fixPathDesplegables(Model uiModel,
			Siniestro siniestro, List<AfectadoDomicilioSiniestro> listaAfectados, List<Trabajo> trabajos) {
		//TODO: ver como se hace esto "bien" (acceso a elemento de lista en 'path')        
        int cont = 1;
        for (AfectadoDomicilioSiniestro ads : listaAfectados) {
        	 uiModel.addAttribute("domicilio-" + cont++, ads.getAdsDomId());
		}
        
        cont = 1;
        for (Trabajo trabajo : trabajos) {
        	 uiModel.addAttribute("trabajo-" + cont++, trabajo);
		}
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
