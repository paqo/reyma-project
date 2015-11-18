package com.reyma.gestion.controller;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

import com.reyma.gestion.busqueda.BusquedaHelper;
import com.reyma.gestion.busqueda.dto.ResultadoBusqueda;
import com.reyma.gestion.busqueda.dto.ResultadoBusquedaCaducados;
import com.reyma.gestion.controller.validators.UtilsValidacion;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Oficio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.dao.Provincia;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.Trabajo;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.EstadoService;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.IvaService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.OperarioService;
import com.reyma.gestion.service.PresupuestoService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.ui.MensajeErrorJson;
import com.reyma.gestion.ui.MensajeExitoJson;
import com.reyma.gestion.ui.listados.ElementoComboDTO;
import com.reyma.gestion.ui.listados.FacturaListadoDTO;
import com.reyma.gestion.ui.listados.PresupuestoListadoDTO;
import com.reyma.gestion.ui.listados.SiniestroListadoDataTablesDTO;
import com.reyma.gestion.util.Fechas;

import flexjson.JSONSerializer;

@RequestMapping("/siniestroes")
@Controller
public class SiniestroController {
	
	/*
	 * esto seria mas para informacion de sesion
	 * (cuantos usuarios hay logados, tiempo, caducarlos...etc)
	 * 
	 */
	/* 
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry; 
	*/
	
	@Autowired
	InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
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
	PresupuestoService presupuestoService;

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
	
	@Autowired
	BusquedaHelper busquedas;
	
	@Autowired
	IvaService ivaService;
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Siniestro siniestro, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	if ( UtilsValidacion.ignorarErrorBooleanBinding(bindingResult) ){
	        	// no es realmente error, valor "on" para el checkbox
				// TODO: modificar la manera en que se hace el binding
				// (no se pueden usar converters de jpa por la version usada)
				siniestro.setSinUrgente((short)1);
			} else {
				populateEditForm(uiModel, siniestro);
	            return "siniestroes/create";
			}            
        }
        uiModel.asMap().clear();
        siniestroService.saveSiniestro(siniestro);
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
        Siniestro siniestro = siniestroService.findSiniestro(sinId);
        uiModel.addAttribute("siniestro", siniestro);
        uiModel.addAttribute("compania", companiaService.findCompania(siniestro.getSinComId().getComId()));
        List<AfectadoDomicilioSiniestro> afectados = afectadoDomicilioSiniestroService.findAfectadosDomicilioByIdSiniestro(sinId);
        
        uiModel.addAttribute("afectados", afectados );
        
        uiModel.addAttribute("itemId", sinId);
        return "siniestroes/show";
    }
	
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, Principal principal) {
		 
		/*
		 * ###### SNIPPET PARA AUTENTICACION Y SESION ##############################		 
		 * 
		 String name = principal.getName();
		 		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				System.out.println("=> usuario: " + user.getUsername() + ", " + user.getPassword());
				
				System.out.println("=> detalles: " + SecurityContextHolder.getContext().getAuthentication().getDetails()  );
				
				List<Object> principals = sessionRegistry.getAllPrincipals();

				for (Object princ: principals) {
				    if (princ instanceof User) {
				       System.out.println("=> usuario encontrado: " + ((User) princ).getUsername());
				    }
				}
		
		System.out.println("=> " + principal.getName());
		
		Authentication user = (Authentication) principal;
		
		//GrantedAuthority rol = new SimpleGrantedAuthority("ROLE_TECNICO");
		
		System.out.println("=> detalles: " + inMemoryUserDetailsManager.loadUserByUsername("reymasur").getAuthorities());
				
		GrantedAuthority rol;
		
		Iterator<? extends GrantedAuthority> it = user.getAuthorities().iterator();
		
		while ( it.hasNext() ){
			rol = it.next();
			//rol.getAuthority();			
			System.out.println("=> " + rol.getAuthority().equalsIgnoreCase("ROLE_TECNICO") );
		}*/
				
		/*
		 * ############## SIN DATATABLES 
		 *
		 if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            List<Siniestro> siniestros = siniestroService.findSiniestroEntries(firstResult, sizeNo, "sinFechaEncargo", "DESC");
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
        	List<Siniestro> siniestros = siniestroService.findAllSiniestroes("sinFechaEncargo", "DESC");
            List<SiniestroListadoDTO> listaSiniestros = new ArrayList<SiniestroListadoDTO>();
            SiniestroListadoDTO dto;
            for (Siniestro siniestro : siniestros) {
				dto = new SiniestroListadoDTO(siniestro, afectadoDomicilioSiniestroService);
				listaSiniestros.add(dto);
			} 
            uiModel.addAttribute("siniestroes", listaSiniestros);
        }*/
		
		return "siniestroes/list";
    }
	
	@RequestMapping(params = "listado", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String listado() {
		
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();					
		Converter<Siniestro, SiniestroListadoDataTablesDTO> converter = acsf.getSiniestroToSiniestroListadoDataTablesDTOConverter();
		
		List<Siniestro> siniestros = siniestroService.findAllSiniestroes("sinFechaEncargo", "DESC");
		List<SiniestroListadoDataTablesDTO> listadoSiniestros = new ArrayList<SiniestroListadoDataTablesDTO>();
        
        for (Siniestro siniestro : siniestros) {		
			listadoSiniestros.add( converter.convert(siniestro) );
		} 
        
        Map<String, List<SiniestroListadoDataTablesDTO>> test = new HashMap<String, List<SiniestroListadoDataTablesDTO>>();
        test.put( "aaData", listadoSiniestros);
        
        return new JSONSerializer().exclude("*.class").deepSerialize(test);
	}
	
	
	@RequestMapping(params = "caducados", produces = "text/html")
    public String listarCaducados(@RequestParam(value = "caducados", required = false) Integer caducados, Model uiModel) {
		
		List<Siniestro> encontrados = siniestroService.findSiniestrosCaducados(caducados);
		List<ResultadoBusquedaCaducados> listaCaducados = new ArrayList<ResultadoBusquedaCaducados>();
		List<ResultadoBusqueda> _resultados = busquedas.obtenerResultadosBusqueda(encontrados);		
		for (ResultadoBusqueda result : _resultados) {
			ResultadoBusquedaCaducados res = new ResultadoBusquedaCaducados(result, 
					Fechas.diasTranscurridosDesde(result.getFecha()));
			listaCaducados.add(res);
		}
		
		uiModel.addAttribute("caducados", listaCaducados );

        return "siniestroes/caducados";
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
        return "redirect:/siniestroes/" + 
        		encodeUrlPathSegment(siniestro.getSinId().toString(), httpServletRequest) + 
        		"?form";
        
    }

	@RequestMapping(value = "/{sinId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("sinId") Integer sinId, Model uiModel) {
        populateEditForm(uiModel, siniestroService.findSiniestro(sinId));
        return "siniestroes/update";
    }
	
	@RequestMapping(value = "/{sinId}", params = "eliminar", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
    public String delete(@PathVariable("sinId") Integer idSiniestro, Model uiModel) {
		JSONSerializer serializer = new JSONSerializer();
		MensajeErrorJson mensajeError = null;
		MensajeExitoJson mensajeExito = null;		
		List<AfectadoDomicilioSiniestro> listaAfectados = adsService.findAfectadosDomicilioByIdSiniestro(idSiniestro);
		try {			
			Siniestro sinBorrar = siniestroService.findSiniestro(idSiniestro);
			//1.- comprobar si el siniestro tiene facturas
			int numFacturas = sinBorrar.getFacturas().size();
			if (numFacturas > 0){
				// si tiene facturas avisar
				mensajeError = new MensajeErrorJson("El siniestro tiene " + numFacturas + " facturas asociadas. "
						+ "Para poder borrar el siniestro, deben eliminarse primero las facturas asociadas.");
				mensajeError.setTitulo("Facturas encontradas para el siniestro");
			} else {
				//2.- borramos las relaciones de la tabla de afectados
				Domicilio dom;
				Persona per;
				for (AfectadoDomicilioSiniestro ads : listaAfectados) {
					dom = ads.getAdsDomId();
					per = ads.getAdsPerId();
					ads.remove();
					// si la persona o domicilio ya no tiene 
					// relacion con otro siniestro, se borra del sistema
					if ( adsService.findAfectadosDomicilioByIdDomicilio(dom.getDomId()).size() == 0 ){
						dom.remove();
					}
					if ( adsService.findAfectadosDomicilioByIdPersona(per.getPerId()).size() == 0 ){
						per.remove();
					}
				}
				//3.- borrar siniestro
				sinBorrar.remove();
				mensajeExito = new MensajeExitoJson("El siniestro se ha eliminado con éxito");
				return serializer.exclude("class").serialize(mensajeExito);
			}			
		} catch (Exception e) {			
			e.printStackTrace();
			mensajeError = new MensajeErrorJson("Se ha producido un error inesperado elimianando el siniestro");
			mensajeError.setTitulo("Error eliminando siniestro");
		}
		return serializer.exclude("class").serialize(mensajeError);
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        /*uiModel.addAttribute("siniestro_sinfechacomunicacion_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("siniestro_sinfechaocurrencia_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));*/
		uiModel.addAttribute("siniestro_sinfechaocurrencia_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
		uiModel.addAttribute("siniestro_sinfechaencargo_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
		uiModel.addAttribute("siniestro_sinfechafin_date_format", Fechas.FORMATO_FECHA_DDMMYYYYHHMM);
    }

	void populateEditForm(Model uiModel, Siniestro siniestro) {
        
		JSONSerializer serializer = new JSONSerializer();
		
		uiModel.addAttribute("siniestro", siniestro);
        addDateTimeFormatPatterns(uiModel);
        ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();
		Converter<Factura, FacturaListadoDTO> facConverter = acsf.getFacturaToFacturaListadoDTOConverter();
		Converter<Presupuesto, PresupuestoListadoDTO> presConverter = acsf.getPresupuestoToPresupuestoListadoDTOConverter();		
        
        List<AfectadoDomicilioSiniestro> afectados = 
        		afectadoDomicilioSiniestroService.findAfectadosDomicilioByIdSiniestro(siniestro.getSinId());
        
        List<Trabajo> trabajos = trabajoService.findTrabajosByIdSiniestro(siniestro.getSinId());
        
        // afectados
        uiModel.addAttribute("afectadodomiciliosiniestroes", afectados);
        // trabajos
        uiModel.addAttribute("trabajos", trabajos);
        
        // estado finalizado para poner fecha automaticamente
        uiModel.addAttribute("idEstadoFinalizado", estadoService.findEstadoByDescripcion("Finalizado").getEstId() );
        
        //############### facturas
        // combo de Ivas
        List<ElementoComboDTO> ivasCbo = new ArrayList<ElementoComboDTO>(); 
        List<Iva> ivas = ivaService.findAllIvas();
        for (Iva iva : ivas) {
        	ivasCbo.add( new ElementoComboDTO(iva.getIvaId(), iva.getIvaValor() + "%") );
		}
        uiModel.addAttribute("valoresCboIva", serializer.exclude("*.class").serialize(ivasCbo));		
        // combo de oficios
        List<Oficio> oficios = oficioService.findAllOficios();        
        List<ElementoComboDTO> oficiosCbo = new ArrayList<ElementoComboDTO>();   
        for (Oficio oficio : oficios) {
        	oficiosCbo.add(  new ElementoComboDTO(oficio.getOfiId(), oficio.getOfiDescripcion()) );
		}
        uiModel.addAttribute("valoresCboOficios", serializer.exclude("*.class").serialize(oficiosCbo));
        // listado de facturas del siniestro 
        List<Factura> facturas = facturaService.findFacturasByIdSiniestro(siniestro.getSinId());        
        List<FacturaListadoDTO> facturasListado = new ArrayList<FacturaListadoDTO>();        
        for (Factura fac : facturas) {
        	facturasListado.add(facConverter.convert(fac));
		}
        uiModel.addAttribute("facturas", facturasListado);     
        // combo ivas
        uiModel.addAttribute("ivas", ivas );        
        //############### fin facturas
        
        //############### presupuestos
        // (combos de oficios e ivas ya están de factura)
        // listado de presupuestos del siniestro 
        List<Presupuesto> presupuestos = presupuestoService.findPresupuestoByIdSiniestro(siniestro.getSinId());  
        List<PresupuestoListadoDTO> presupuestosListado = new ArrayList<PresupuestoListadoDTO>();        
        for (Presupuesto pres : presupuestos) {
        	presupuestosListado.add(presConverter.convert(pres));
		}
        uiModel.addAttribute("presupuestos", presupuestosListado);        
        //############### fin presupuestos
        
        // desplegables
        uiModel.addAttribute("companias", companiaService.findAllCompanias());
        uiModel.addAttribute("estadoes", estadoService.findAllEstadoes());
        uiModel.addAttribute("tiposiniestroes", tipoSiniestroService.findAllTipoSiniestroes());
        
        uiModel.addAttribute("provincias", provinciaService.findAllProvincias());
        uiModel.addAttribute("municipios", municipioService.findAllMunicipiosByIdProvincia(41));
        // ponemos por defecto sevilla como provincia y municipio        
        Domicilio domicilio = new Domicilio();
        Provincia prov = provinciaService.findProvinciaByDescripcion("Sevilla", false); 
        Municipio mun = municipioService.findMunicipioByIdProvinciaAndDesc(prov.getPrvId(), "Sevilla");
        
        //TODO: temporal para evitar error cuando se duplican municipios
        // ver porque se pueden duplicar y controlar bien por codigo
        if ( mun == null ){
        	mun = municipioService.findMunicipio(757); // -> Sevilla
        }
        
        domicilio.setDomMunId(mun);
        domicilio.setDomProvId(mun.getMunPrvId());        
        uiModel.addAttribute("domicilio", domicilio);
        uiModel.addAttribute("municipios", municipioService.findAllMunicipiosByIdProvincia(41));
        
        uiModel.addAttribute("trabajo-1", new Trabajo());
        uiModel.addAttribute("oficios", oficios );
        uiModel.addAttribute("operarios", operarioService.findAllOperarios() );
        
        fixPathDesplegables(uiModel, siniestro, afectados, trabajos);
    }

	private void fixPathDesplegables(Model uiModel, Siniestro siniestro, 
			List<AfectadoDomicilioSiniestro> listaAfectados, List<Trabajo> trabajos) {
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
