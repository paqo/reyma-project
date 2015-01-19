package com.reyma.gestion.integracion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.reyma.gestion.controller.ApplicationConversionServiceFactoryBean;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.CodigoPostal;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Provincia;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.dao.TipoSiniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoSiniestroService;

@Service
public class IntegracionHelper {

	@Autowired
	private SiniestroService siniestroService;
	
	@Autowired
	private TipoSiniestroService tipoSiniestroService;
	
	@Autowired
	private ProvinciaService provinciaService;
	
	@Autowired
	private MunicipioService municipioService;
	
	@Autowired
	private DomicilioService domicilioService;
	
	@Autowired
	private PersonaService personaService;
		
	@Autowired
	private AfectadoDomicilioSiniestroService adsService;
	
	public static final TipoAfectacion TIPO_AFEC_ASEGURADO;
	public static final TipoAfectacion TIPO_AFEC_PERJUDICADO;
	public static final TipoAfectacion TIPO_AFEC_AMBOS;
	
	static {
		ApplicationConversionServiceFactoryBean acsf = new ApplicationConversionServiceFactoryBean();
		Converter<String, TipoAfectacion> converter = acsf.getDescripcionToTipoAfectacionConverter();
		TIPO_AFEC_AMBOS = converter.convert("AMBOS");
		TIPO_AFEC_ASEGURADO = converter.convert("ASEGURADO");
		TIPO_AFEC_PERJUDICADO = converter.convert("PERJUDICADO");
	}
	
	//TODO: incluir en el mapeo (con value)
	// el tipo de compania
	//TODO: ver si se puede mandar a google 
	// que formatee las direcciones para los
	// municipios y las provincias
	/*
	 * mapeo de parametros:
	 * - compania: viene del propio script
	 * - estado: inicial por defecto (recibido)
	 * - tipo: $("#l_DescripPlanosValue").text() -> los valores
	 * que vienen en ppio son: fontaneria, albañileria, 
	 * pintura, carpinteria de ****... (startswith, contains...) [ENCARGO]
	 * - num siniestro: $("#HeaderAssignment1_lblSiniestro").text() [SINIESTRO]
	 * - num poliza: $("#l_NumPolizaValue").text() [POLIZA]
	 * - fecha ocurrencia: $("#l_FechOcurrValue").text() [SINIESTRO]
	 * - fecha comunicacion: $("#l_FechDeclaracionValue").text() [SINIESTRO]
	 * - descripcion: $("#l_VersionValue").text() [SINIESTRO]
	 * - observaciones: $("#Label1").text() [ENCARGO] incluirlo en descripcion? 
	 * - urgente: $("#chk_UrgenteValue").is(":checked") [ENCARGO]
	 * -------------------------------------------------
	 * @ ASEGURADO: 
	 * -nombre: $("#l_NombreTomadorValue").text() [POLIZA]
	 * -nif/cif: $("#l_NIFCIFValue").text() [POLIZA]
	 * -direccion: $("#l_DirecTomadorValue").text() [POLIZA] (incluye normalmente
	 * el municipio y la provincia, formatear lo que se pueda)
	 * -provincia: Sevilla por defecto
	 * -municipio: extraer de la direccion (si se puede)
	 * -CP: extraer de la direccion (expresion regular 41***)		 * 
	 * -tel1 y tel2: $("#l_TelTomadorValue").text() [POLIZA] (extraer para
	 * separar si se puede) 
	 * @ PERJUDICADO:
	 * - nombre: $("#l_NombreLugarValue").text() [ENCARGO]
	 * - nif/cif no hay
	 * - direccion: $("#HeaderAssignment1_lblDir_Riesgo").text() [GENERAL]
	 * - tel1: $("#l_TelefLugarValue").text() [ENCARGO] (quitar '(fijo)')
	 * - tel2: $("#l_TelefLugarValue2").text() [ENCARGO] (quitar '(movil)')
	 * - CP: extraer de la direccion (expresion regular 41***)
	 * - provincia: Sevilla por defecto
	 * - municipio: extraer de la direccion (si se puede)
	 *   para extraer, quizas: $("#l_DireccionLugarVaue") [ENCARGO]
	 */
	public MensajeResultadoIntegracion procesarPeticion( Siniestro siniestro, Domicilio domicilio, Persona asegurado, 
			PerjudicadoDTO perjudicado, HttpServletRequest request ) {
				
		System.out.println("=> siniestro: " + siniestro);
		
		
		//TODO: ver que se hace si no se puede obtener el CP (alguno de los dos)
		
		MensajeResultadoIntegracion msg = new MensajeResultadoIntegracion();
		
		//comprobar si ya existe el siniestro
		Siniestro sinCheck = siniestroService.findSiniestroByNumSiniestro( siniestro.getSinNumero() );
		if ( sinCheck != null ){	
			msg.setResultado("existe");
			msg.setDescripcion("El siniestro con número '" + siniestro.getSinNumero() + "' ya existe en ReymaSur.");
			return msg;
		}
		
		boolean resultado = false;
		// tipo siniestro
		TipoSiniestro tipoSiniestro = obtenerTipoSiniestro(request.getParameter("tipoSiniestro"));		
		siniestro.setSinTsiId(tipoSiniestro);
		// descripcion a minúsculas
		siniestro.setSinDescripcion( StringUtils.capitalize(siniestro.getSinDescripcion().toLowerCase()) );
		// observaciones
		anyadirObservaciones(request.getParameter("obs"), siniestro);
		try {
			// crear datos generales del siniestro			
			siniestroService.saveSiniestro(siniestro);
			// crear datos de afectados
			// 1.- añadir asegurado (en cualquier caso)
			// la prov y el municipio del asegurado vienen
			// solamente con la descripcion, buscar para
			// añadir
			anyadirProvyMunADomicilio(domicilio, request, new String[]{"provId","munId"}); 
			// añadir el asegurado
			AfectadoDomicilioSiniestro ads1 = anyadirAsegurado(siniestro, domicilio, asegurado);	
			// 2.- añadir perjudicado (si es distinto)
			// es distinto?
			if (request.getParameter("mismolugar") != null && 
					"true".equalsIgnoreCase( request.getParameter("mismolugar") )){
				// es el mismo, hemos terminado
				resultado = true;
			} else {
				// asegurado y perjudicado distintos
				ads1.setAdsTafId(TIPO_AFEC_ASEGURADO);	
				ads1.merge();
				anyadirPerjudicado(perjudicado, siniestro, request);				
				resultado = true;
			}
		} catch (Exception e) {
			// TODO log4j?
			e.printStackTrace();
			resultado = false;
		}
				
		if ( resultado ){
			msg.setResultado("creado");
			msg.setDescripcion("siniestroes/" + siniestro.getSinId() + "?form");
		} else {
			msg.setResultado("error");
			msg.setDescripcion("siniestroes/" + siniestro.getSinId() + "?form");
		}	
		return msg;
	}
	
	private AfectadoDomicilioSiniestro anyadirPerjudicado(PerjudicadoDTO perjudicado, Siniestro siniestro,
			HttpServletRequest request) {
		AfectadoDomicilioSiniestro ads = new AfectadoDomicilioSiniestro();
		Domicilio _domicilio = new Domicilio();
		_domicilio.setDomCp(perjudicado.getPerjCp());
		_domicilio.setDomDireccion(perjudicado.getPerjDireccion());
		anyadirProvyMunADomicilio(_domicilio, request, new String[]{"perjProvId", "perjMunId"});
		Persona adsPersona = grabarPersona(perjudicado);
		if ( adsPersona != null ){ // grabar persona			
			Domicilio adsDomicilio = grabarDomicilio(_domicilio);
			if ( adsDomicilio != null ) { // grabar domicilio
				if ( grabarAfectadoDomicilioSiniestro(ads, adsPersona, adsDomicilio, 
						siniestro, TIPO_AFEC_PERJUDICADO)){
					return ads;
				} else {
					// error
				}
			} else {
				// error guardar domicilio
			}
		} else {
			// error afectado
		}
		return null;
	}

	private Persona grabarPersona(PerjudicadoDTO perjudicado) {
		Persona _persona = new Persona();
		_persona.setPerNombre(perjudicado.getPerjNombre());
		_persona.setPerTlf1(perjudicado.getPerjTlf1());
		_persona.setPerTlf2(perjudicado.getPerjTlf2());
		return grabarPersona(_persona);
	}

	private AfectadoDomicilioSiniestro anyadirAsegurado(Siniestro siniestro, Domicilio domicilio,
			Persona asegurado) {
		AfectadoDomicilioSiniestro ads1 = new AfectadoDomicilioSiniestro();
		Persona adsPersona = grabarPersona(asegurado);
		if ( adsPersona != null ){ // grabar persona							
			Domicilio adsDomicilio = grabarDomicilio(domicilio);
			if ( adsDomicilio != null ) { // grabar domicilio
				if ( grabarAfectadoDomicilioSiniestro(ads1, adsPersona, adsDomicilio, siniestro, 
						TIPO_AFEC_AMBOS /* inicialmente consideramos que ambos */)){
					return ads1;
				} else {
					// error
				}
			} else {
				// error guardar domicilio
			}
		} else {
			// error afectado
		}
		return null;
	}
	
	private void anyadirProvyMunADomicilio(Domicilio domicilio, HttpServletRequest request, String[] requestParamsNames) {
		String prov = request.getParameter(requestParamsNames[0]);
		String mun = request.getParameter(requestParamsNames[1]);
		Provincia provincia = null;
		Municipio municipio = null;
		if ( StringUtils.isNotBlank(prov) ){
			provincia = provinciaService.findProvinciaByDescripcion(prov, true);
		} 
		if ( provincia == null || StringUtils.isEmpty(prov) ){
			provincia = provinciaService.findProvinciaByDescripcion("Sevilla", true);	
		}
		domicilio.setDomProvId(provincia);
		
		List<CodigoPostal> codigosPostales = CodigoPostal.findByCodPostal(String.valueOf(domicilio.getDomCp()) );		
		if ( codigosPostales != null && codigosPostales.size() > 0 ){
			CodigoPostal codigoPostal = codigosPostales.get(0);
			if (codigosPostales.size() > 1){
				int distLev = Integer.MAX_VALUE;
				for (CodigoPostal _codigoPostal : codigosPostales) {
					if ( StringUtils.getLevenshteinDistance(_codigoPostal.getCpMunicipio(), mun) < distLev ){
						codigoPostal = _codigoPostal;
					}
				}
			}
			municipio = municipioService.findMunicipioByIdProvinciaAndDesc(provincia.getPrvId(), codigoPostal.getCpMunicipio());
		} else {
			//TODO:No se ha podido encontrar el CP, intentamos buscar
			// municipio mediante la descripcion
			
		}
		
		// busqueda por nombre (fallaría con todos los que no sean de una sola palabra) 
		/*List<Municipio> municipios = municipioService.findMunicipiosByIdProvAndDesc(provincia.getPrvId(), mun);
		if ( municipios != null ){
			Municipio munTemp = municipios.get(0);
			if ( municipios.size() > 1  ){				
				int distLev = Integer.MAX_VALUE;
				for (Municipio _municipio : municipios) {
					if ( StringUtils.getLevenshteinDistance(mun, _municipio.getMunDescripcion()) < distLev ){
						munTemp = _municipio;
					}
				}				
			}
			municipio = munTemp;
		}*/
		
		// si se ha podido encontrar el municipio
		// (desde la importacion no se crea)
		if ( municipio != null ){
			domicilio.setDomMunId(municipio);
		}
	}
	
	private void anyadirObservaciones(String obs, Siniestro siniestro) {
		if ( StringUtils.isNotBlank(obs) ){
			siniestro.setSinDescripcion(
					siniestro.getSinDescripcion() + "\n\n" + obs
				);
		}		
	}
	
	private TipoSiniestro obtenerTipoSiniestro(String tipoRaw) {
				
		if ( StringUtils.isNotEmpty(tipoRaw) ){ 
			if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"fontaneria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Fontaneria");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"electricidad") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Electricidad");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"carpinteria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Carpinteria");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"albañileria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Albañileria");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase(), 
					"pintura") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Pintura");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase(), 
					"marmolisteria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Marmolisteria");
			}
		}
		return null;
	}
	
	public boolean grabarAfectadoDomicilioSiniestro(
			AfectadoDomicilioSiniestro ads, Persona persona,
			Domicilio domicilio, Siniestro sin, TipoAfectacion ta) {
		ads.setAdsPerId(persona);
		ads.setAdsDomId(domicilio);
		ads.setAdsSinId(sin);
		ads.setAdsTafId(ta);
		try {
			adsService.saveAfectadoDomicilioSiniestro(ads);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Persona grabarPersona(Persona persona) {
		Persona personaEnc = null;		
		try {
			personaEnc = personaService.findPersona(persona);
			if ( personaEnc == null){
				// nueva persona, grabar
				personaService.savePersona(persona);
			} else {
				// persona encontrada, comprobar si han
				// cambiado los tlfs y en su caso grabar para no perder
				// esa info
				if ( !StringUtils.equals(persona.getPerTlf1(), personaEnc.getPerTlf1()) ||
					 !StringUtils.equals(persona.getPerTlf2(), personaEnc.getPerTlf2()) ){
					personaService.savePersona(persona);					
				} else {
					// persona ya almacenda, devolvemos la encontrada
					return personaEnc;				
				}
			}
			return persona;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Domicilio grabarDomicilio(Domicilio domicilio) {
		try {			
			Domicilio domBusqueda = domicilioService.findDomicilio(domicilio);
			if ( domBusqueda == null ){
				domicilioService.saveDomicilio(domicilio);
				return domicilio;
			} else {
				// domicilio encontrado, devolvemos el persistido
				return domBusqueda;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * ------------------------------------------------
	 * alternativa para extraer CP:
	 * $("#l_DireccionLugarVaue") [ENCARGO]
	 */
}
