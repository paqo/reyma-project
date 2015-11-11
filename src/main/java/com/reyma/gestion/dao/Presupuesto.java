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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.util.Fechas;

@Entity
@Table(name = "PRESUPUESTO")
@Configurable
public class Presupuesto {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Presupuesto().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPresupuesto() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Presupuesto o", Long.class).getSingleResult();
    }

	public static List<Presupuesto> findAllPresupuestos() {
        return entityManager().createQuery("SELECT o FROM Presupuesto o", Presupuesto.class).getResultList();
    }

	public static List<Presupuesto> findAllPresupuesto(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Presupuesto o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Presupuesto.class).getResultList();
    }

	public static Presupuesto findPresupuesto(Integer presId) {
        if (presId == null) return null;
        return entityManager().find(Presupuesto.class, presId);
    }

	public static List<Presupuesto> findPresupuestoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Presupuesto o", Presupuesto.class)
        		.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Presupuesto> findPresupuestoEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Presupuesto o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || 
            	"DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Presupuesto.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Presupuesto attached = Presupuesto.findPresupuesto(this.presId);
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
    public Presupuesto merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Presupuesto merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        				.setExcludeFieldNames("lineaPresupuestos", "presSinId").toString();
    }
	
	public static List<Presupuesto> findPresupuestoParaSiniestro(Integer id) {
		 String jpaQuery = "SELECT o FROM Presupuesto o WHERE o.presSinId = " + id;	        
	     return entityManager().createQuery(jpaQuery, Presupuesto.class).getResultList();
	}	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pres_id")
    private Integer presId;

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
	@OneToMany(mappedBy = "linPresId",  orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<LineaPresupuesto> lineaPresupuesto;

	@ManyToOne
    @JoinColumn(name = "pres_sin_id", referencedColumnName = "sin_id", nullable = false)
    private Siniestro presSinId;
	
	@OneToOne
	@JoinColumn(name = "pres_ads_id", referencedColumnName = "ads_id", nullable = false)
	private AfectadoDomicilioSiniestro presAdsId;

	@Column(name = "pres_fecha")
    @Temporal(TemporalType.TIMESTAMP)    
	@DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar presFecha;
	
	@Column(name = "pres_num_presupuesto")	
	private String presNumPresupuesto;
	
	public Set<LineaPresupuesto> getLineaPresupuesto() {
		return lineaPresupuesto;
	}

	public void setLineaPresupuesto(Set<LineaPresupuesto> lineaPresupuesto) {
		this.lineaPresupuesto = lineaPresupuesto;
	}

	public String getPresNumPresupuesto() {
		return presNumPresupuesto;
	}

	public void setPresNumPresupuesto(String presNumPresupuesto) {
		this.presNumPresupuesto = presNumPresupuesto;
	}

	public Siniestro getPresSinId() {
		return presSinId;
	}

	public void setPresSinId(Siniestro presSinId) {
		this.presSinId = presSinId;
	}

	public AfectadoDomicilioSiniestro getPresAdsId() {
		return presAdsId;
	}

	public void setPresAdsId(AfectadoDomicilioSiniestro presAdsId) {
		this.presAdsId = presAdsId;
	}

	public Calendar getPresFecha() {
		return presFecha;
	}

	public void setPresFecha(Calendar presFecha) {
		this.presFecha = presFecha;
	}

	public Integer getPresId() {
		return presId;
	}

	public void setPresId(Integer presId) {
		this.presId = presId;
	}	
}
