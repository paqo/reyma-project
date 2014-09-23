package com.reyma.gestion.dao;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "TRABAJO")
public class Trabajo {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Trabajo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTrabajoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Trabajo o", Long.class).getSingleResult();
    }

	public static List<Trabajo> findAllTrabajoes() {
        return entityManager().createQuery("SELECT o FROM Trabajo o", Trabajo.class).getResultList();
    }

	public static List<Trabajo> findAllTrabajoes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Trabajo o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Trabajo.class).getResultList();
    }

	public static Trabajo findTrabajo(Integer traId) {
        if (traId == null) return null;
        return entityManager().find(Trabajo.class, traId);
    }

	public static List<Trabajo> findTrabajoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Trabajo o", Trabajo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Trabajo> findTrabajoEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Trabajo o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Trabajo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Trabajo attached = Trabajo.findTrabajo(this.traId);
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
    public Trabajo merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Trabajo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("traOfiId", "traOpeId", "traSinId").toString();
    }

	@ManyToOne
    @JoinColumn(name = "tra_ofi_id", referencedColumnName = "ofi_id", nullable = false)
    private Oficio traOfiId;

	@ManyToOne
    @JoinColumn(name = "tra_ope_id", referencedColumnName = "ope_id", nullable = false)
    private Operario traOpeId;

	@ManyToOne
    @JoinColumn(name = "tra_sin_id", referencedColumnName = "sin_id", nullable = false)
    private Siniestro traSinId;

	@Column(name = "tra_fecha")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar traFecha;

	public Oficio getTraOfiId() {
        return traOfiId;
    }

	public void setTraOfiId(Oficio traOfiId) {
        this.traOfiId = traOfiId;
    }

	public Operario getTraOpeId() {
        return traOpeId;
    }

	public void setTraOpeId(Operario traOpeId) {
        this.traOpeId = traOpeId;
    }

	public Siniestro getTraSinId() {
        return traSinId;
    }

	public void setTraSinId(Siniestro traSinId) {
        this.traSinId = traSinId;
    }

	public Calendar getTraFecha() {
        return traFecha;
    }

	public void setTraFecha(Calendar traFecha) {
        this.traFecha = traFecha;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tra_id")
    private Integer traId;

	public Integer getTraId() {
        return this.traId;
    }

	public void setTraId(Integer id) {
        this.traId = id;
    }
}
