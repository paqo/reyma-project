package com.reyma.gestion.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Compania;
import com.reyma.gestion.dao.Domicilio;
import com.reyma.gestion.dao.Estado;
import com.reyma.gestion.dao.Factura;
import com.reyma.gestion.dao.Iva;
import com.reyma.gestion.dao.LineaFactura;
import com.reyma.gestion.dao.LineaPresupuesto;
import com.reyma.gestion.dao.Municipio;
import com.reyma.gestion.dao.Oficio;
import com.reyma.gestion.dao.Operario;
import com.reyma.gestion.dao.Persona;
import com.reyma.gestion.dao.Presupuesto;
import com.reyma.gestion.dao.Provincia;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.dao.TipoAfectacion;
import com.reyma.gestion.dao.TipoSiniestro;
import com.reyma.gestion.dao.Trabajo;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;
import com.reyma.gestion.service.CompaniaService;
import com.reyma.gestion.service.DomicilioService;
import com.reyma.gestion.service.EstadoService;
import com.reyma.gestion.service.FacturaService;
import com.reyma.gestion.service.IvaService;
import com.reyma.gestion.service.LineaFacturaService;
import com.reyma.gestion.service.MunicipioService;
import com.reyma.gestion.service.OficioService;
import com.reyma.gestion.service.OperarioService;
import com.reyma.gestion.service.PersonaService;
import com.reyma.gestion.service.ProvinciaService;
import com.reyma.gestion.service.SiniestroService;
import com.reyma.gestion.service.TipoAfectacionService;
import com.reyma.gestion.service.TipoSiniestroService;
import com.reyma.gestion.service.TrabajoService;
import com.reyma.gestion.ui.LineaFacturaDTO;
import com.reyma.gestion.ui.LineaPresupuestoDTO;
import com.reyma.gestion.ui.listados.FacturaListadoDTO;
import com.reyma.gestion.ui.listados.PresupuestoListadoDTO;
import com.reyma.gestion.ui.listados.SiniestroListadoDataTablesDTO;
import com.reyma.gestion.util.Fechas;
import com.reyma.gestion.util.Utils;

import flexjson.JSONDeserializer;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    AfectadoDomicilioSiniestroService afectadoDomicilioSiniestroService;

	@Autowired
    CompaniaService companiaService;

	@Autowired
    DomicilioService domicilioService;

	@Autowired
    EstadoService estadoService;

	@Autowired
    FacturaService facturaService;

	@Autowired
    IvaService ivaService;

	@Autowired
    LineaFacturaService lineaFacturaService;

	@Autowired
    MunicipioService municipioService;

	@Autowired
    OficioService oficioService;

	@Autowired
    OperarioService operarioService;

	@Autowired
    PersonaService personaService;

	@Autowired
    ProvinciaService provinciaService;

	@Autowired
    SiniestroService siniestroService;

	@Autowired
    TipoAfectacionService tipoAfectacionService;

	@Autowired
    TipoSiniestroService tipoSiniestroService;

	@Autowired
    TrabajoService trabajoService;

	public Converter<AfectadoDomicilioSiniestro, String> getAfectadoDomicilioSiniestroToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.AfectadoDomicilioSiniestro, java.lang.String>() {
            public String convert(AfectadoDomicilioSiniestro afectadoDomicilioSiniestro) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Integer, AfectadoDomicilioSiniestro> getIdToAfectadoDomicilioSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.AfectadoDomicilioSiniestro>() {
            public com.reyma.gestion.dao.AfectadoDomicilioSiniestro convert(java.lang.Integer id) {
                return afectadoDomicilioSiniestroService.findAfectadoDomicilioSiniestro(id);
            }
        };
    }

	public Converter<String, AfectadoDomicilioSiniestro> getStringToAfectadoDomicilioSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.AfectadoDomicilioSiniestro>() {
            public com.reyma.gestion.dao.AfectadoDomicilioSiniestro convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), AfectadoDomicilioSiniestro.class);
            }
        };
    }

	public Converter<Compania, String> getCompaniaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Compania, java.lang.String>() {
            public String convert(Compania compania) {
                return new StringBuilder().append(compania.getComDescripcion()).append(' ').append(compania.getComCodigo()).toString();
            }
        };
    }

	public Converter<Integer, Compania> getIdToCompaniaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Compania>() {
            public com.reyma.gestion.dao.Compania convert(java.lang.Integer id) {
                return companiaService.findCompania(id);
            }
        };
    }

	public Converter<String, Compania> getStringToCompaniaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Compania>() {
            public com.reyma.gestion.dao.Compania convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Compania.class);
            }
        };
    }

	public Converter<Domicilio, String> getDomicilioToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Domicilio, java.lang.String>() {
            public String convert(Domicilio domicilio) {
                return new StringBuilder().append(domicilio.getDomDireccion()).append(' ').append(domicilio.getDomCp()).toString();
            }
        };
    }

	public Converter<Integer, Domicilio> getIdToDomicilioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Domicilio>() {
            public com.reyma.gestion.dao.Domicilio convert(java.lang.Integer id) {
                return domicilioService.findDomicilio(id);
            }
        };
    }

	public Converter<String, Domicilio> getStringToDomicilioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Domicilio>() {
            public com.reyma.gestion.dao.Domicilio convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Domicilio.class);
            }
        }; 
    }

	public Converter<Estado, String> getEstadoToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Estado, java.lang.String>() {
            public String convert(Estado estado) {
                return new StringBuilder().append(estado.getEstDescripcion()).toString();
            }
        };
    }

	public Converter<Integer, Estado> getIdToEstadoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Estado>() {
            public com.reyma.gestion.dao.Estado convert(java.lang.Integer id) {
                return estadoService.findEstado(id);
            }
        };
    }

	public Converter<String, Estado> getStringToEstadoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Estado>() {
            public com.reyma.gestion.dao.Estado convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Estado.class);
            }
        };
    }

	public Converter<Factura, String> getFacturaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Factura, java.lang.String>() {
            public String convert(Factura factura) {
                return new StringBuilder().append(factura.getFacFecha()).toString();
            }
        };
    }

	public Converter<Integer, Factura> getIdToFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Factura>() {
            public com.reyma.gestion.dao.Factura convert(java.lang.Integer id) {
                return facturaService.findFactura(id);
            }
        };
    }
	
	public Converter<Factura, FacturaListadoDTO> getFacturaToFacturaListadoDTOConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Factura, com.reyma.gestion.ui.listados.FacturaListadoDTO>() {
            public FacturaListadoDTO convert(Factura factura) {
            	String fecha = Fechas.formatearFechaDDMMYYYYHHMM( factura.getFacFecha().getTime() );            	
                return new FacturaListadoDTO(factura.getFacId(), factura.getFacAdsId().getAdsId(), factura.getFacNumFactura(), fecha);
            }
        };
    }
	
	public Converter<LineaFactura, LineaFacturaDTO> getLineaFacturaToLineaFacturaListadoDTOConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.LineaFactura, com.reyma.gestion.ui.LineaFacturaDTO>() {
            public LineaFacturaDTO convert(LineaFactura lineaFactura) {            	        	
                return new LineaFacturaDTO(lineaFactura.getLinId(), lineaFactura.getLinConcepto(), 
                		lineaFactura.getLinIvaId().getIvaId(), 
                		lineaFactura.getLinImporte().doubleValue(), 
                		lineaFactura.getLinOficioId().getOfiId());
            }
        };
    }
	
	public Converter<LineaFacturaDTO, LineaFactura> getLineaFacturaDTOToLineaFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<LineaFacturaDTO, LineaFactura>() {
            public LineaFactura convert(LineaFacturaDTO lineaDto) {   
            	LineaFactura res = new LineaFactura();
            	res.setLinId(lineaDto.getId());
            	res.setLinConcepto( lineaDto.getConcepto() );
            	res.setLinImporte( new BigDecimal(lineaDto.getCoste()) );            	
            	res.setLinIvaId( ivaService.findIva(lineaDto.getIva()) );         
            	res.setLinOficioId(oficioService.findOficio(lineaDto.getOficio())); 
                return res;
            }
        };
    }
	
	public Converter<Siniestro, SiniestroListadoDataTablesDTO> getSiniestroToSiniestroListadoDataTablesDTOConverter() {
        return new org.springframework.core.convert.converter.Converter<Siniestro, SiniestroListadoDataTablesDTO>() {
            public SiniestroListadoDataTablesDTO convert(Siniestro siniestro) {   
            	SiniestroListadoDataTablesDTO res = new SiniestroListadoDataTablesDTO();            	
            	res.setId(siniestro.getSinId());
            	res.setCompania(siniestro.getSinComId().getComCodigo());
            	res.setNumeroSiniestro(siniestro.getSinNumero());
            	res.setFecha( Fechas.formatearFechaDDMMYYYY(siniestro.getSinFechaEncargo().getTime() ) );
            	// direccion y afectados (comprobar afectados, perjudicados...etc)            	
            	Utils.cargarAfectadosYDomicilios(siniestro, res, afectadoDomicilioSiniestroService);            	
                return res;
            }
        };
    }
	
	public Converter<String, List<LineaFactura>> getLineaFacturaJsonStringToLineasFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, List<LineaFactura>>() {
            public List<LineaFactura>convert(String lineasJsonStr) {            
            	List<LineaFactura> lineas = new JSONDeserializer<ArrayList<LineaFactura>>()
       				 .use("values", LineaFactura.class).deserialize(lineasJsonStr, ArrayList.class);
            	Iva auxIva;
            	Oficio auxOficio;
            	for (LineaFactura lineaFactura : lineas) {
            		auxIva = ivaService.findIva( lineaFactura.getLinIvaId().getIvaId() );
            		auxOficio = oficioService.findOficio(lineaFactura.getLinOficioId().getOfiId() );
            		lineaFactura.setLinIvaId(auxIva);
            		lineaFactura.setLinOficioId(auxOficio);
				}
            	return lineas;
            }
        };
    }
	
	public Converter<String, List<LineaPresupuesto>> getLineaPresupuestoJsonStringToLineasPresupuestoConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, List<LineaPresupuesto>>() {
            public List<LineaPresupuesto>convert(String lineasJsonStr) {            
            	List<LineaPresupuesto> lineas = new JSONDeserializer<ArrayList<LineaPresupuesto>>()
       				 .use("values", LineaPresupuesto.class).deserialize(lineasJsonStr, ArrayList.class);
            	Iva auxIva;
            	Oficio auxOficio;
            	for (LineaPresupuesto lineaPresupuesto : lineas) {
            		auxIva = ivaService.findIva( lineaPresupuesto.getLinIvaId().getIvaId() );
            		auxOficio = oficioService.findOficio(lineaPresupuesto.getLinOficioId().getOfiId() );
            		lineaPresupuesto.setLinIvaId(auxIva);
            		lineaPresupuesto.setLinOficioId(auxOficio);
				}
            	return lineas;
            }
        };
	}

	public Converter<Presupuesto, PresupuestoListadoDTO> getPresupuestoToPresupuestoListadoDTOConverter() {
		 return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Presupuesto, com.reyma.gestion.ui.listados.PresupuestoListadoDTO>() {
	            public PresupuestoListadoDTO convert(Presupuesto presupuesto) {
	            	String fecha = Fechas.formatearFechaDDMMYYYY( presupuesto.getPresFecha().getTime() );
	                return new PresupuestoListadoDTO(presupuesto.getPresId(), 
	                		presupuesto.getPresAdsId().getAdsId(), presupuesto.getPresNumPresupuesto(), fecha);
	            }
	        };
	}

	public Converter<String, Factura> getStringToFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Factura>() {
            public com.reyma.gestion.dao.Factura convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Factura.class);
            }
        };
    }

	public Converter<Iva, String> getIvaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Iva, java.lang.String>() {
            public String convert(Iva iva) {
                return new StringBuilder().append(iva.getIvaValor()).toString();
            }
        };
    }

	public Converter<Integer, Iva> getIdToIvaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Iva>() {
            public com.reyma.gestion.dao.Iva convert(java.lang.Integer id) {
                return ivaService.findIva(id);
            }
        };
    }

	public Converter<String, Iva> getStringToIvaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Iva>() {
            public com.reyma.gestion.dao.Iva convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Iva.class);
            }
        };
    }

	public Converter<LineaFactura, String> getLineaFacturaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.LineaFactura, java.lang.String>() {
            public String convert(LineaFactura lineaFactura) {
                return new StringBuilder().append(lineaFactura.getLinConcepto()).append(' ').append(lineaFactura.getLinImporte()).toString();
            }
        };
    }

	public Converter<Integer, LineaFactura> getIdToLineaFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.LineaFactura>() {
            public com.reyma.gestion.dao.LineaFactura convert(java.lang.Integer id) {
                return lineaFacturaService.findLineaFactura(id);
            }
        };
    }

	public Converter<String, LineaFactura> getStringToLineaFacturaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.LineaFactura>() {
            public com.reyma.gestion.dao.LineaFactura convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), LineaFactura.class);
            }
        };
    }

	public Converter<Municipio, String> getMunicipioToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Municipio, java.lang.String>() {
            public String convert(Municipio municipio) {
                return new StringBuilder().append(municipio.getMunDescripcion()).toString();
            }
        };
    }

	public Converter<Integer, Municipio> getIdToMunicipioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Municipio>() {
            public com.reyma.gestion.dao.Municipio convert(java.lang.Integer id) {
                return municipioService.findMunicipio(id);
            }
        };
    }

	public Converter<String, Municipio> getStringToMunicipioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Municipio>() {
            public com.reyma.gestion.dao.Municipio convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Municipio.class);
            }
        };
    }

	public Converter<Oficio, String> getOficioToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Oficio, java.lang.String>() {
            public String convert(Oficio oficio) {
                return new StringBuilder().append(oficio.getOfiDescripcion()).toString();
            }
        };
    }

	public Converter<Integer, Oficio> getIdToOficioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Oficio>() {
            public com.reyma.gestion.dao.Oficio convert(java.lang.Integer id) {
                return oficioService.findOficio(id);
            }
        };
    }

	public Converter<String, Oficio> getStringToOficioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Oficio>() {
            public com.reyma.gestion.dao.Oficio convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Oficio.class);
            }
        };
    }

	public Converter<Operario, String> getOperarioToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Operario, java.lang.String>() {
            public String convert(Operario operario) {
                return new StringBuilder().append(operario.getOpeNombrePila()).append(' ').append(operario.getOpeNombre()).toString();
            }
        };
    }

	public Converter<Integer, Operario> getIdToOperarioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Operario>() {
            public com.reyma.gestion.dao.Operario convert(java.lang.Integer id) {
                return operarioService.findOperario(id);
            }
        };
    }

	public Converter<String, Operario> getStringToOperarioConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Operario>() {
            public com.reyma.gestion.dao.Operario convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Operario.class);
            }
        };
    }

	public Converter<Persona, String> getPersonaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Persona, java.lang.String>() {
            public String convert(Persona persona) {
                return new StringBuilder().append(persona.getPerNombre()).append(' ').append(persona.getPerNif()).append(' ').append(persona.getPerTlf1()).append(' ').append(persona.getPerTlf2()).toString();
            }
        };
    }

	public Converter<Integer, Persona> getIdToPersonaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Persona>() {
            public com.reyma.gestion.dao.Persona convert(java.lang.Integer id) {
                return personaService.findPersona(id);
            }
        };
    }

	public Converter<String, Persona> getStringToPersonaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Persona>() {
            public com.reyma.gestion.dao.Persona convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Persona.class);
            }
        };
    }

	public Converter<Provincia, String> getProvinciaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Provincia, java.lang.String>() {
            public String convert(Provincia provincia) {
                return new StringBuilder().append(provincia.getPrvDescripcion()).toString();
            }
        };
    }

	public Converter<Integer, Provincia> getIdToProvinciaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Provincia>() {
            public com.reyma.gestion.dao.Provincia convert(java.lang.Integer id) {
                return provinciaService.findProvincia(id);
            }
        };
    }

	public Converter<String, Provincia> getStringToProvinciaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Provincia>() {
            public com.reyma.gestion.dao.Provincia convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Provincia.class);
            }
        };
    }

	public Converter<Siniestro, String> getSiniestroToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Siniestro, java.lang.String>() {
            public String convert(Siniestro siniestro) {
                return new StringBuilder().append(siniestro.getSinNumero()).append(' ').append(siniestro.getSinPoliza()).append(' ')
                		.append(siniestro.getSinFechaEncargo()).append(' ').append(siniestro.getSinFechaOcurrencia()).toString();
            }
        };
    }

	public Converter<Integer, Siniestro> getIdToSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Siniestro>() {
            public com.reyma.gestion.dao.Siniestro convert(java.lang.Integer id) {
                return siniestroService.findSiniestro(id);
            }
        };
    }

	public Converter<String, Siniestro> getStringToSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Siniestro>() {
            public com.reyma.gestion.dao.Siniestro convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Siniestro.class);
            }
        };
    }

	public Converter<TipoAfectacion, String> getTipoAfectacionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.TipoAfectacion, java.lang.String>() {
            public String convert(TipoAfectacion tipoAfectacion) {
                return new StringBuilder().append(tipoAfectacion.getTafDescripcion()).toString();
            }
        };
    }

	public Converter<Integer, TipoAfectacion> getIdToTipoAfectacionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.TipoAfectacion>() {
            public com.reyma.gestion.dao.TipoAfectacion convert(java.lang.Integer id) {
                return tipoAfectacionService.findTipoAfectacion(id);
            }
        };
    }

	public Converter<String, TipoAfectacion> getStringToTipoAfectacionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.TipoAfectacion>() {
            public com.reyma.gestion.dao.TipoAfectacion convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), TipoAfectacion.class);
            }
        };
    }
	
	public Converter<String, TipoAfectacion> getDescripcionToTipoAfectacionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.TipoAfectacion>() {
            public com.reyma.gestion.dao.TipoAfectacion convert(String tipo) {
            	return tipoAfectacionService.findTipoAfectacionByDesc(tipo);
            }
        };
    }

	public Converter<TipoSiniestro, String> getTipoSiniestroToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.TipoSiniestro, java.lang.String>() {
            public String convert(TipoSiniestro tipoSiniestro) {
                return new StringBuilder().append(tipoSiniestro.getTsiDescripcion()).toString();
            }
        };
    }
	
	public Converter<Integer, TipoSiniestro> getIdToTipoSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.TipoSiniestro>() {
            public com.reyma.gestion.dao.TipoSiniestro convert(java.lang.Integer id) {
                return tipoSiniestroService.findTipoSiniestro(id);
            }
        };
    }

	public Converter<String, TipoSiniestro> getStringToTipoSiniestroConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.TipoSiniestro>() {
            public com.reyma.gestion.dao.TipoSiniestro convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), TipoSiniestro.class);
            }
        };
    }

	public Converter<Trabajo, String> getTrabajoToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.Trabajo, java.lang.String>() {
            public String convert(Trabajo trabajo) {
                return new StringBuilder().append(trabajo.getTraFecha()).toString();
            }
        };
    }

	public Converter<Integer, Trabajo> getIdToTrabajoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, com.reyma.gestion.dao.Trabajo>() {
            public com.reyma.gestion.dao.Trabajo convert(java.lang.Integer id) {
                return trabajoService.findTrabajo(id);
            }
        };
    }

	public Converter<String, Trabajo> getStringToTrabajoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.reyma.gestion.dao.Trabajo>() {
            public com.reyma.gestion.dao.Trabajo convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Trabajo.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAfectadoDomicilioSiniestroToStringConverter());
        registry.addConverter(getIdToAfectadoDomicilioSiniestroConverter());
        registry.addConverter(getStringToAfectadoDomicilioSiniestroConverter());
        registry.addConverter(getCompaniaToStringConverter());
        registry.addConverter(getIdToCompaniaConverter());
        registry.addConverter(getStringToCompaniaConverter());
        registry.addConverter(getDomicilioToStringConverter());
        registry.addConverter(getIdToDomicilioConverter());
        registry.addConverter(getStringToDomicilioConverter());
        registry.addConverter(getEstadoToStringConverter());
        registry.addConverter(getIdToEstadoConverter());
        registry.addConverter(getStringToEstadoConverter());
        registry.addConverter(getFacturaToStringConverter());
        registry.addConverter(getIdToFacturaConverter());
        registry.addConverter(getStringToFacturaConverter());
        registry.addConverter(getIvaToStringConverter());
        registry.addConverter(getIdToIvaConverter());
        registry.addConverter(getStringToIvaConverter());
        registry.addConverter(getLineaFacturaToStringConverter());
        registry.addConverter(getIdToLineaFacturaConverter());
        registry.addConverter(getStringToLineaFacturaConverter());
        registry.addConverter(getMunicipioToStringConverter());
        registry.addConverter(getIdToMunicipioConverter());
        registry.addConverter(getStringToMunicipioConverter());
        registry.addConverter(getOficioToStringConverter());
        registry.addConverter(getIdToOficioConverter());
        registry.addConverter(getStringToOficioConverter());
        registry.addConverter(getOperarioToStringConverter());
        registry.addConverter(getIdToOperarioConverter());
        registry.addConverter(getStringToOperarioConverter());
        registry.addConverter(getPersonaToStringConverter());
        registry.addConverter(getIdToPersonaConverter());
        registry.addConverter(getStringToPersonaConverter());
        registry.addConverter(getProvinciaToStringConverter());
        registry.addConverter(getIdToProvinciaConverter());
        registry.addConverter(getStringToProvinciaConverter());
        registry.addConverter(getSiniestroToStringConverter());
        registry.addConverter(getIdToSiniestroConverter());
        registry.addConverter(getStringToSiniestroConverter());
        registry.addConverter(getTipoAfectacionToStringConverter());
        registry.addConverter(getIdToTipoAfectacionConverter());
        registry.addConverter(getStringToTipoAfectacionConverter());
        registry.addConverter(getTipoSiniestroToStringConverter());
        registry.addConverter(getIdToTipoSiniestroConverter());
        registry.addConverter(getStringToTipoSiniestroConverter());
        registry.addConverter(getTrabajoToStringConverter());
        registry.addConverter(getIdToTrabajoConverter());
        registry.addConverter(getStringToTrabajoConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

	public Converter<LineaPresupuesto, LineaPresupuestoDTO> getLineaPresupuestoToLineaPresupuestoListadoDTOConverter() {
		return new org.springframework.core.convert.converter.Converter<com.reyma.gestion.dao.LineaPresupuesto, com.reyma.gestion.ui.LineaPresupuestoDTO>() {
            public LineaPresupuestoDTO convert(LineaPresupuesto lineaPresupuesto) {            	        	
                // distinguir entre cabeceras y conceptos
            	return lineaPresupuesto.getLinOficioId() != null?
            			new LineaPresupuestoDTO( // linea con cabecera
            					lineaPresupuesto.getLinId(), 
            					lineaPresupuesto.getLinOficioId().getOfiId()            					
            			) : 
            			new LineaPresupuestoDTO( // linea real de concepto
            					lineaPresupuesto.getLinId(), lineaPresupuesto.getLinConcepto(), 
            					lineaPresupuesto.getLinIvaId().getIvaId(), 
            					lineaPresupuesto.getLinImporte().doubleValue()
            			);
            }
        };
	}	

}
