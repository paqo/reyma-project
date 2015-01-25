package com.reyma.gestion.ui.listados;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.reyma.gestion.dao.AfectadoDomicilioSiniestro;
import com.reyma.gestion.dao.Siniestro;
import com.reyma.gestion.service.AfectadoDomicilioSiniestroService;

public class SiniestroListadoDTO implements Serializable {

	private static final long serialVersionUID = -1234908986577868450L;
	
	private Integer id;
	private String compania;
	private String numeroSiniestro;
	private String direccion;
	private String afectado;
	private Calendar fecha;	
		
	public SiniestroListadoDTO() {
		super();
	}
	
	public SiniestroListadoDTO( Siniestro sin, AfectadoDomicilioSiniestroService adsService) {
		super();
		id = sin.getSinId();
		compania = sin.getSinComId().getComCodigo();
		numeroSiniestro = sin.getSinNumero();
		fecha = sin.getSinFechaEncargo();
		List<AfectadoDomicilioSiniestro> afectados = adsService.findAfectadosDomicilioByIdSiniestro(id);
		StringBuilder sbAfec = new StringBuilder();
		StringBuilder sbDom = new StringBuilder();
		Set<Integer> domicilios = new HashSet<Integer>();
		Set<Integer> perjudicados = new HashSet<Integer>();
		for (AfectadoDomicilioSiniestro afectado : afectados) {
			if ( afectado.getAdsTafId().getTafId() == 4 || 
				 afectado.getAdsTafId().getTafId() == 5 ){ //TODO: id de bd (perjudicado)
				if ( !domicilios.contains( afectado.getAdsDomId().getDomId() ) ){
					sbDom.append( afectado.getAdsDomId().getDomDireccion() + ", " );
					domicilios.add(afectado.getAdsDomId().getDomId());
				}
				if ( !perjudicados.contains(afectado.getAdsPerId().getPerId()) ){
					sbAfec.append( afectado.getAdsPerId().getPerNombre() + ", " );
					perjudicados.add(afectado.getAdsPerId().getPerId());
				}
			}
		}
		afectado = sbAfec.length() >= 2? // limpiar si solo es ", " 
				sbAfec.substring(0, sbAfec.length()-2) : "";
		direccion = sbDom.length() >= 2? sbDom.substring(0, sbDom.length()-2) : "";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroSiniestro() {
		return numeroSiniestro;
	}

	public void setNumeroSiniestro(String numeroSiniestro) {
		this.numeroSiniestro = numeroSiniestro;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getAfectado() {
		return afectado;
	}

	public void setAfectado(String afectado) {
		this.afectado = afectado;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getCompania() {
		return compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}
	
}
