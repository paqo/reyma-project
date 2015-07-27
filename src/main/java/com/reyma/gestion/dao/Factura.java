package com.reyma.gestion.dao;
import java.util.Calendar;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.util.Fechas;

@Entity
@Table(name = "FACTURA")
@Configurable
public class Factura {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fac_id")
    private Integer facId;

	public Integer getFacId() {
        return this.facId;
    }

	public void setFacId(Integer id) {
        this.facId = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Factura().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countFacturas() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Factura o", Long.class).getSingleResult();
    }

	public static List<Factura> findAllFacturas() {
        return entityManager().createQuery("SELECT o FROM Factura o", Factura.class).getResultList();
    }

	public static List<Factura> findAllFacturas(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Factura o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Factura.class).getResultList();
    }

	public static Factura findFactura(Integer facId) {
        if (facId == null) return null;
        return entityManager().find(Factura.class, facId);
    }

	public static List<Factura> findFacturaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Factura o", Factura.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Factura> findFacturaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Factura o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Factura.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Factura attached = Factura.findFactura(this.facId);
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
    public Factura merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Factura merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("lineaFacturas", "facSinId").toString();
    }

	/*
	 * explicacion diferencia entre orphanRemoval=true, cascade=CascadeType.REMOVE
	 * http://nsinfra.blogspot.in/2013/03/jpa-20-hibernate-orphanremoval-true.html
	 * 
	 * (basicamente: con cascade=CascadeType.REMOVE, al borrar de la colección de
	 * objetos de entidades hijas que tiene el objeto de la entidad padre, no 
	 * borraría las entidades hijas, solamente se borrarían al borrar el propio padre, 
	 * mientras que con orphanremoval=true, al borrar de la colección del padre, 
	 * se borrarían también las entidades hijas
	 * 
	 * NOTA: es necesario poner CascadeType.ALL para que funcione con todas las operaciones, 
	 * con CascadeType.PERSIST, por ejemplo, no valdría para los merge
	 */
	@OneToMany(mappedBy = "linFacId",  orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<LineaFactura> lineaFacturas;

	@ManyToOne
    @JoinColumn(name = "fac_sin_id", referencedColumnName = "sin_id", nullable = false)
    private Siniestro facSinId;
	
	@OneToOne
	@JoinColumn(name = "fac_ads_id", referencedColumnName = "ads_id", nullable = false)
	private AfectadoDomicilioSiniestro facAdsId;

	@Column(name = "fac_fecha")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)    
	@DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar facFecha;
	
	@Column(name = "fac_num_factura")	
	private String facNumFactura;

	public Set<LineaFactura> getLineaFacturas() {
        return lineaFacturas;
    }

	public void setLineaFacturas(Set<LineaFactura> lineaFacturas) {
        this.lineaFacturas = lineaFacturas;
    }

	public Siniestro getFacSinId() {
        return facSinId;
    }

	public void setFacSinId(Siniestro facSinId) {
        this.facSinId = facSinId;
    }

	public Calendar getFacFecha() {
        return facFecha;
    }

	public void setFacFecha(Calendar facFecha) {
        this.facFecha = facFecha;
    }
	
	public String getFacNumFactura() {
		return facNumFactura;
	}

	public void setFacNumFactura(String facNumFactura) {
		this.facNumFactura = facNumFactura;
	}
	
	public AfectadoDomicilioSiniestro getFacAdsId() {
		return facAdsId;
	}

	public void setFacAdsId(AfectadoDomicilioSiniestro facAdsId) {
		this.facAdsId = facAdsId;
	}

	public static List<Factura> findFacturasParaSiniestro(Integer id) {
		 String jpaQuery = "SELECT o FROM Factura o WHERE o.facSinId = " + id;	        
	     return entityManager().createQuery(jpaQuery, Factura.class).getResultList();
	}	
}
