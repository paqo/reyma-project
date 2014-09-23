package com.reyma.gestion.dao;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
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
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

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

	@OneToMany(mappedBy = "linFacId")
    private Set<LineaFactura> lineaFacturas;

	@ManyToOne
    @JoinColumn(name = "fac_sin_id", referencedColumnName = "sin_id", nullable = false)
    private Siniestro facSinId;

	@Column(name = "fac_fecha")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar facFecha;

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
}
