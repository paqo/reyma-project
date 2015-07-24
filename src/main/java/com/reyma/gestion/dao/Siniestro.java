package com.reyma.gestion.dao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.controller.SiniestroController;
import com.reyma.gestion.util.Fechas;

@Configurable
@Entity
@Table(name = "SINIESTRO")
public class Siniestro {

	@PersistenceContext
    transient EntityManager entityManager;
	
	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("sinTsiId", "sinFechaEncargo");

	public static final EntityManager entityManager() {
        EntityManager em = new Siniestro().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSiniestroes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Siniestro o", Long.class).getSingleResult();
    }

	public static List<Siniestro> findAllSiniestroes() {
        return entityManager().createQuery("SELECT o FROM Siniestro o", Siniestro.class).getResultList();
    }
	
	public static List<Siniestro> findSiniestrosParaFecha(Calendar fecha) {
        return entityManager().createQuery("SELECT o FROM Siniestro o "
				+ "WHERE o.sinFechaEncargo = :fecha", Siniestro.class)
				.setParameter("fecha", fecha)
				.getResultList();
    }
	
	public static List<Siniestro> findSiniestrosEntreFechas(Calendar fechaIni, Calendar fechaFin) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Siniestro> cq = cb.createQuery(Siniestro.class);
        final Root<Siniestro> siniestroRoot = cq.from(Siniestro.class);        
       
        Predicate condicion = cb.between(siniestroRoot.<Calendar>get("sinFechaEncargo"), fechaIni, fechaFin);
        
		CriteriaQuery<Siniestro> query = 
        		cq.select(siniestroRoot).
            	where(condicion).
            	distinct(true).
            	orderBy( cb.asc(siniestroRoot.get("sinComId")),
            			cb.desc(siniestroRoot.get("sinFechaEncargo")) ); 
		
		return entityManager().createQuery( query ).getResultList(); 
    }
	
	public static List<Siniestro> findSiniestrosCaducados(int dias, Compania compania) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Siniestro> cq = cb.createQuery(Siniestro.class);
        final Root<Siniestro> siniestroRoot = cq.from(Siniestro.class);
        
        Calendar fechaCad = Fechas.getFechaHoy(true);
        fechaCad.add(GregorianCalendar.DATE, dias * (-1) );
                
        List<Predicate> condiciones = new ArrayList<Predicate>();
        condiciones.add(
	        cb.lessThanOrEqualTo(siniestroRoot.<Calendar>get("sinFechaEncargo"), fechaCad)
	    );
        // estado distinto de finalizado                
        condiciones.add(cb.notEqual(siniestroRoot.get("sinEstId"), 
        							Estado.findEstadoByDescripcion("Finalizado")));
        // si hay compania definida
        if ( compania != null ){
        	 condiciones.add(cb.equal(siniestroRoot.get("sinComId"), compania));
        }         
		CriteriaQuery<Siniestro> query = 
        		cq.select(siniestroRoot).
            	where(condiciones.toArray(new Predicate[]{}) ).
            	distinct(true).
            	orderBy( cb.asc(siniestroRoot.get("sinComId")),
            			cb.asc(siniestroRoot.get("sinFechaEncargo")) ); 
		
		return entityManager().createQuery( query ).getResultList(); 
    }

	public static List<Siniestro> findAllSiniestroes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Siniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        
        // return entityManager().createNativeQuery("SELECT * FROM SINIESTRO order by sin_fecha_comunicacion desc", Siniestro.class).getResultList();
        
        return entityManager().createQuery(jpaQuery, Siniestro.class).getResultList();
    }

	public static Siniestro findSiniestro(Integer sinId) {
        if (sinId == null) return null;
        return entityManager().find(Siniestro.class, sinId);
    }
	
	public static Siniestro findSiniestroByNumSiniestro(String sinNumero) {
        if (sinNumero == null) return null;  
        Siniestro res;
		try {
			res = entityManager().createQuery("SELECT o FROM Siniestro o "
							+ "WHERE o.sinNumero = :numero", Siniestro.class)
							.setParameter("numero", sinNumero)
							.getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}       
        return res;      
    }

	public static List<Siniestro> findSiniestroEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Siniestro o", Siniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Siniestro> findSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Siniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Siniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Siniestro attached = Siniestro.findSiniestro(this.sinId);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Siniestro merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Siniestro merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("afectadoDomicilioSiniestroes", "facturas", "trabajoes", "sinComId", "sinEstId", "sinTsiId").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sin_id")
    private Integer sinId;

	public Integer getSinId() {
        return this.sinId;
    }

	public void setSinId(Integer id) {
        this.sinId = id;
    }

	@OneToMany(mappedBy = "adsSinId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes;

	@OneToMany(mappedBy = "facSinId")
    private Set<Factura> facturas;

	@OneToMany(mappedBy = "traSinId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Trabajo> trabajoes;

	@ManyToOne
    @JoinColumn(name = "sin_com_id", referencedColumnName = "com_id")
    private Compania sinComId;

	@ManyToOne
    @JoinColumn(name = "sin_est_id", referencedColumnName = "est_id")
    private Estado sinEstId;

	@ManyToOne
    @JoinColumn(name = "sin_tsi_id", referencedColumnName = "tsi_id")
    private TipoSiniestro sinTsiId;

	@Column(name = "sin_numero", length = 50, unique = true)
    @NotNull
    private String sinNumero;

	@Column(name = "sin_poliza", length = 50)
    private String sinPoliza;

	@Column(name = "sin_fecha_encargo")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar sinFechaEncargo;

	@Column(name = "sin_fecha_ocurrencia")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar sinFechaOcurrencia;
	
	@Column(name = "sin_fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar sinFechaFin;

	@Column(name = "sin_descripcion", length = 500)
    private String sinDescripcion;

	@Column(name = "sin_observaciones", length = 250)
    private String sinObservaciones;
	
	@Column(name = "sin_mediador", length = 70)
    private String sinMediador;

	public String getSinMediador() {
		return sinMediador;
	}

	public void setSinMediador(String sinMediador) {
		this.sinMediador = sinMediador;
	}

	@Column(name = "sin_urgente")
    private Short sinUrgente;

	public Set<AfectadoDomicilioSiniestro> getAfectadoDomicilioSiniestroes() {
        return afectadoDomicilioSiniestroes;
    }

	public void setAfectadoDomicilioSiniestroes(Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes) {
        this.afectadoDomicilioSiniestroes = afectadoDomicilioSiniestroes;
    }

	public Set<Factura> getFacturas() {
        return facturas;
    }

	public void setFacturas(Set<Factura> facturas) {
        this.facturas = facturas;
    }

	public Set<Trabajo> getTrabajoes() {
        return trabajoes;
    }

	public void setTrabajoes(Set<Trabajo> trabajoes) {
        this.trabajoes = trabajoes;
    }

	public Compania getSinComId() {
        return sinComId;
    }

	public void setSinComId(Compania sinComId) {
        this.sinComId = sinComId;
    }

	public Estado getSinEstId() {
        return sinEstId;
    }

	public void setSinEstId(Estado sinEstId) {
        this.sinEstId = sinEstId;
    }

	public TipoSiniestro getSinTsiId() {
        return sinTsiId;
    }

	public void setSinTsiId(TipoSiniestro sinTsiId) {
        this.sinTsiId = sinTsiId;
    }

	public String getSinNumero() {
        return sinNumero;
    }

	public void setSinNumero(String sinNumero) {
        this.sinNumero = sinNumero;
    }

	public String getSinPoliza() {
        return sinPoliza;
    }

	public void setSinPoliza(String sinPoliza) {
        this.sinPoliza = sinPoliza;
    }
		
	public Calendar getSinFechaEncargo() {
		return sinFechaEncargo;
	}

	public void setSinFechaEncargo(Calendar sinFechaEncargo) {
		this.sinFechaEncargo = sinFechaEncargo;
	}

	public Calendar getSinFechaOcurrencia() {
        return sinFechaOcurrencia;
    }

	public void setSinFechaOcurrencia(Calendar sinFechaOcurrencia) {
        this.sinFechaOcurrencia = sinFechaOcurrencia;
    }

	public Calendar getSinFechaFin() {
		return sinFechaFin;
	}

	public void setSinFechaFin(Calendar sinFechaFin) {
		this.sinFechaFin = sinFechaFin;
	}

	public String getSinDescripcion() {
        return sinDescripcion;
    }

	public void setSinDescripcion(String sinDescripcion) {
        this.sinDescripcion = sinDescripcion;
    }

	public String getSinObservaciones() {
        return sinObservaciones;
    }

	public void setSinObservaciones(String sinObservaciones) {
        this.sinObservaciones = sinObservaciones;
    }

	public Short getSinUrgente() {
        return sinUrgente;
    }

	public void setSinUrgente(Short sinUrgente) {
        this.sinUrgente = sinUrgente;
    }

	public static List<Siniestro> buscarSiniestrosPorCriterios(Siniestro siniestro, Domicilio domicilio, 
			Persona afectado, Map<String, Object> parametrosAdicionales) {
		if (siniestro == null) return null;
       
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Siniestro> cq = cb.createQuery(Siniestro.class);
        final Root<Siniestro> siniestroRoot = cq.from(Siniestro.class);
        
        List<Predicate> condiciones = new ArrayList<Predicate>();
        Join<Siniestro, AfectadoDomicilioSiniestro> joinAds = null;
        
        //1.- condiciones generales del siniestro
        if ( anyadirCondicionesParaEntidad(siniestro, parametrosAdicionales) ){        	
            obtenerCondicionesSiniestro(siniestro, parametrosAdicionales, siniestroRoot, condiciones, cb); 
        }     
        
        if ( anyadirCondicionesParaEntidad(domicilio, null) ||
        		anyadirCondicionesParaEntidad(afectado, null) ){
        	joinAds = siniestroRoot.join("afectadoDomicilioSiniestroes");
        }
        
        //2.- si hay condiciones del domicilio        
        obtenerCondicionesDomicilio(domicilio, siniestroRoot, joinAds, condiciones, cb);
        
        //3.- si hay condiciones de asegurado/perjudicado
        obtenerCondicionesAfectado(afectado, siniestroRoot, joinAds, condiciones, cb);
        
        CriteriaQuery<Siniestro> query = 
        		cq.select(siniestroRoot).
            	where( condiciones.toArray(new Predicate[]{}) ).
            	distinct(true).
            	orderBy( cb.asc(siniestroRoot.get("sinComId")),
            			cb.desc(siniestroRoot.get("sinFechaEncargo")) ); 
        
        return entityManager().createQuery( query ).getResultList();  
	}
	
	private static void obtenerCondicionesAfectado(Persona afectado,
			Root<Siniestro> siniestroRoot,
			Join<Siniestro, AfectadoDomicilioSiniestro> joinAds,
			List<Predicate> condiciones, CriteriaBuilder cb) {
		
		if ( joinAds == null  ){
			return;
		}
		Predicate condicion;
		Expression<String> exp, exp2;		
		if ( !StringUtils.isEmpty(afectado.getPerNombre()) ){			
			exp = joinAds.get("adsPerId").get("perNombre");
	    	condicion = cb.like(exp, "%" + afectado.getPerNombre()+ "%");
	    	condiciones.add(condicion);		
		}
		if ( !StringUtils.isEmpty(afectado.getPerNif()) ){
			exp = joinAds.get("adsPerId").get("perNif");
	    	condicion = cb.like(exp, "%" + afectado.getPerNif()+ "%");
	    	condiciones.add(condicion);		
		}
		if ( !StringUtils.isEmpty(afectado.getPerTlf1()) ||
				!StringUtils.isEmpty(afectado.getPerTlf2())){
			/*exp = joinAds.get("adsPerId").get("perTlf1");
	    	condicion = cb.like(exp, "%" + afectado.getPerTlf1() + "%");	    	
	    	condiciones.add(condicion);*/			
			exp = joinAds.get("adsPerId").get("perTlf1");
			exp2 = joinAds.get("adsPerId").get("perTlf2");				    	
			condicion = cb.or(cb.like(exp, "%" + afectado.getPerTlf1() + "%"), 
							  cb.like(exp2, "%" + afectado.getPerTlf1() + "%") // tlf1 las dos veces, solo se busca un valor de
							  												   // telefono cada vez
						);	    	
	    	condiciones.add(condicion);
			
		}
	}

	private static void obtenerCondicionesDomicilio(Domicilio domicilioCondiciones, 
			Root<Siniestro> siniestroRoot, Join<Siniestro, AfectadoDomicilioSiniestro> joinAds, 
			List<Predicate> condiciones, CriteriaBuilder cb){
		
		if ( joinAds == null  ){
			return;
		}
		Predicate condicion;
		Expression<String> exp;		
		if ( !StringUtils.isEmpty(domicilioCondiciones.getDomDireccion()) ){
			exp = joinAds.get("adsDomId").get("domDireccion");
	    	condicion = cb.like(exp, "%" + domicilioCondiciones.getDomDireccion() + "%");
	    	condiciones.add(condicion);		
		}
	}
	
	private static List<Predicate> obtenerCondicionesSiniestro(Siniestro siniestroCondiciones, Map<String, Object> parametrosAdicionales, 
			Root<Siniestro> siniestroRoot, List<Predicate> condiciones, CriteriaBuilder cb){
		
		Predicate condicion;
		Expression<String> exp;
		
		if ( !StringUtils.isEmpty(siniestroCondiciones.getSinNumero()) ){
			exp = siniestroRoot.get("sinNumero");			
			condicion = cb.like( exp, "%" + siniestroCondiciones.getSinNumero() + "%");
			condiciones.add(condicion);
		}
		
		if ( !StringUtils.isEmpty(siniestroCondiciones.getSinPoliza() ) ){
			condicion = cb.equal(siniestroRoot.get("sinPoliza"), siniestroCondiciones.getSinPoliza());
			condiciones.add(condicion);
		}
		
		if ( siniestroCondiciones.getSinComId() != null ){
			condicion = cb.equal(siniestroRoot.get("sinComId"), siniestroCondiciones.getSinComId());
			condiciones.add(condicion);
		}

		if ( parametrosAdicionales.containsKey("fechaIni") ){
			Calendar cal1 = (Calendar)parametrosAdicionales.get("fechaIni");
			if ( parametrosAdicionales.containsKey("fechaFin") ){ // periodo
				Calendar cal2 = (Calendar)parametrosAdicionales.get("fechaFin");
				condicion = cb.between(siniestroRoot.<Calendar>get("sinFechaEncargo"), cal1, cal2);
			} else { // fecha exacta
				condicion = cb.equal(siniestroRoot.<Calendar>get("sinFechaEncargo"), cal1);				
			}
			condiciones.add(condicion);
		}
		
		return condiciones;
	}
	
	/**
	 * Método para comprobar si se debe hacer join (crer un objeto
	 * de tipo Root) con esa entidad
	 * @param dao
	 * @return
	 */
	private static boolean anyadirCondicionesParaEntidad(Object dao, Map<String, Object> parametrosAdicionales) {
		if ( dao != null ){
			if ( dao.getClass().isAssignableFrom(Siniestro.class) ){
				Siniestro sin = (Siniestro) dao;
				return !StringUtils.isEmpty(sin.getSinNumero() ) || 
						!StringUtils.isEmpty(sin.getSinPoliza() ) || 
						(sin.getSinComId() != null && sin.getSinComId().getComId() != null ) || 
						parametrosAdicionales.containsKey("fechaIni");
			} else if ( dao.getClass().isAssignableFrom(Domicilio.class) ){
				Domicilio dom = (Domicilio) dao;
				return !StringUtils.isEmpty(dom.getDomDireccion());
			} else if ( dao.getClass().isAssignableFrom(Persona.class) ){
				Persona per = (Persona) dao;
				return !StringUtils.isEmpty(per.getPerNombre()) ||
						!StringUtils.isEmpty(per.getPerNif()) || 
						!StringUtils.isEmpty(per.getPerTlf1()) ||
						!StringUtils.isEmpty(per.getPerTlf2());
			}	
		}			
		return false;
	}
		
}
