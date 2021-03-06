package com.reyma.gestion.dao;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "LINEA_PRESUPUESTO")
public class LineaPresupuesto {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new LineaPresupuesto().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLineasPresupuesto() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LineaPresupuesto o", Long.class).getSingleResult();
    }

	public static List<LineaPresupuesto> findAllLineasPresupuesto() {
        return entityManager().createQuery("SELECT o FROM LineaPresupuesto o "
        								 + " ORDER BY o.linId ", LineaPresupuesto.class).getResultList();
    }
	
	public static List<LineaPresupuesto> findLineaPresupuestoByIdPresupuesto(Integer idPresupuesto) {
        return entityManager().createQuery("SELECT o FROM LineaPresupuesto o WHERE o.linPresId = " + 
        			   idPresupuesto + " ORDER BY o.linId ", LineaPresupuesto.class).getResultList();
    }

	public static List<LineaPresupuesto> findAllLineasPresupuesto(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LineaPresupuesto o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LineaPresupuesto.class).getResultList();
    }

	public static LineaPresupuesto findLineaPresupuesto(Integer linId) {
        if (linId == null) return null;
        return entityManager().find(LineaPresupuesto.class, linId);
    }

	public static List<LineaPresupuesto> findLineaPresupuestoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LineaPresupuesto o", LineaPresupuesto.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LineaPresupuesto> findLineaPresupuestoEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LineaPresupuesto o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LineaPresupuesto.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            LineaPresupuesto attached = LineaPresupuesto.findLineaPresupuesto(this.linId);
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
    public LineaPresupuesto merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LineaPresupuesto merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("linPresId", "linIvaId").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lin_id")
    private Integer linId;

	public Integer getLinId() {
        return this.linId;
    }

	public void setLinId(Integer id) {
        this.linId = id;
    }

	@ManyToOne
    @JoinColumn(name = "lin_pres_id", referencedColumnName = "pres_id", nullable = false)
    private Presupuesto linPresId;

	@ManyToOne
    @JoinColumn(name = "lin_iva_id", referencedColumnName = "iva_id")
    private Iva linIvaId;
	
	@ManyToOne
    @JoinColumn(name = "lin_ofi_id", referencedColumnName = "ofi_id")
    private Oficio linOficioId;

	@Column(name = "lin_concepto", length = 500)
    @NotNull
    private String linConcepto;
		
	@Column(name = "lin_importe", precision = 10, scale = 2)
    @NotNull
    private BigDecimal linImporte;

	public Presupuesto getLinPresId() {
		return linPresId;
	}

	public void setLinPresId(Presupuesto linPresId) {
		this.linPresId = linPresId;
	}

	public Iva getLinIvaId() {
        return linIvaId;
    }

	public void setLinIvaId(Iva linIvaId) {
        this.linIvaId = linIvaId;
    }
	
	public Oficio getLinOficioId() {
		return linOficioId;
	}

	public void setLinOficioId(Oficio linOficioId) {
		this.linOficioId = linOficioId;
	}

	public String getLinConcepto() {
        return linConcepto;
    }

	public void setLinConcepto(String linConcepto) {
        this.linConcepto = linConcepto;
    }

	public BigDecimal getLinImporte() {
        return linImporte;
    }

	public void setLinImporte(BigDecimal linImporte) {
        this.linImporte = linImporte;
    }
}
