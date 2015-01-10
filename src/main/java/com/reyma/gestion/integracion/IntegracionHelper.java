package com.reyma.gestion.integracion;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.reyma.gestion.controller.ApplicationConversionServiceFactoryBean;
import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
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
	
	private static final TipoAfectacion TIPO_AFEC_ASEGURADO;
	private static final TipoAfectacion TIPO_AFEC_PERJUDICADO;
	private static final TipoAfectacion TIPO_AFEC_AMBOS;
	
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
	public MensajeResultadoIntegracion procesarPeticion( Siniestro siniestro, Domicilio domicilio, Persona asegurado, HttpServletRequest request ) {
				
		System.out.println("=> siniestro: " + siniestro);
		
		MensajeResultadoIntegracion msg = new MensajeResultadoIntegracion();
		
		//comprobar si ya existe el siniestro
		Siniestro sinCheck = siniestroService.findSiniestroByNumSiniestro( siniestro.getSinNumero() );
		if ( sinCheck != null ){	
			msg.setResultado("existe");
			msg.setDescripcion("El siniestro con número '" + siniestro.getSinNumero() + "' ya existe en ReymaSur.");
			return msg;
		}
		
		// tipo siniestro
		TipoSiniestro tipoSiniestro = obtenerTipoSiniestro(request.getParameter("tipoSiniestro"));		
		siniestro.setSinTsiId(tipoSiniestro);
		// descripcion a minúsculas
		siniestro.setSinDescripcion( StringUtils.capitalize(siniestro.getSinDescripcion().toLowerCase()) );
		// observaciones
		anyadirObservaciones(request.getParameter("obs"), siniestro);
		// crear datos generales del siniestro
		siniestroService.saveSiniestro(siniestro);
		// crear datos de afectados
		// 1.- añadir asegurado (en cualquier caso)
		AfectadoDomicilioSiniestro ads1 = 
				anyadirAsegurado(siniestro, domicilio, asegurado, request);
		
		
		adsService.saveAfectadoDomicilioSiniestro(ads1);
		
		
		
		msg.setResultado("creado");
		msg.setDescripcion("siniestroes/" + siniestro.getSinId() + "?form");
		return msg;
	}

	private AfectadoDomicilioSiniestro anyadirAsegurado(Siniestro siniestro, Domicilio domicilio,
			Persona asegurado, HttpServletRequest request) {
		AfectadoDomicilioSiniestro ads1 = new AfectadoDomicilioSiniestro();
		anyadirProvyMunADomicilio(domicilio, request);
		Domicilio _domicilio = domicilioService.findDomicilio(domicilio);
		
		//TODO: ***** POR DONDE VOY: ******
		// el domicilio hay que buscarlo por descripcion, el id viene vacio
		// siempre y no lo va a encontrar
		if ( _domicilio == null ){
			domicilioService.saveDomicilio(_domicilio);
		}
		ads1.setAdsDomId(domicilio);
		Persona _asegurado = personaService.findPersona(asegurado);
		if ( _asegurado == null ){
			personaService.savePersona(_asegurado);
		}
		ads1.setAdsPerId(_asegurado);
		ads1.setAdsSinId(siniestro); // (ya existe)		
		ads1.setAdsTafId(TIPO_AFEC_AMBOS); // inicialmente ambos
		return ads1;
	}
	
	private void anyadirProvyMunADomicilio(Domicilio domicilio, HttpServletRequest request) {
		String prov = request.getParameter("provId");
		String mun = request.getParameter("munId");
		Provincia provincia = null;
		Municipio municipio = null;
		if ( StringUtils.isNotBlank(prov) ){
			provincia = provinciaService.findProvinciaByDescripcion(prov, true);
		} 
		if ( provincia == null || StringUtils.isEmpty(prov) ){
			provincia = provinciaService.findProvinciaByDescripcion("Sevilla", true);	
		}
		domicilio.setDomProvId(provincia);
		municipio = municipioService.findMunicipioByIdProvinciaAndDesc(provincia.getPrvId(), mun);
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
				return tipoSiniestroService.findTipoSiniestroByDesc("Fontanería");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"electricidad") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Electricidad");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"carpinteria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Carpintería");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase().replaceAll("í", "i"), 
					"albañileria") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Albañilería");
			} else if ( StringUtils.contains(tipoRaw. 
					trim().toLowerCase(), 
					"pintura") ){
				return tipoSiniestroService.findTipoSiniestroByDesc("Pintura");
			}
		}
		return null;
	}
	/*
	 * -------------------------------------------------	 *  
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
	
	public static void main(String[] args) {
		StringUtils.getLevenshteinDistance("", "");
	}
}
