package com.reyma.gestion.dao;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "IVA")
@Configurable
public class Iva {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("lineaFacturas").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "iva_id")
    private Integer ivaId;

	public Integer getIvaId() {
        return this.ivaId;
    }

	public void setIvaId(Integer id) {
        this.ivaId = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Iva().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countIvas() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Iva o", Long.class).getSingleResult();
    }

	public static List<Iva> findAllIvas() {
        return entityManager().createQuery("SELECT o FROM Iva o", Iva.class).getResultList();
    }

	public static List<Iva> findAllIvas(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Iva o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Iva.class).getResultList();
    }

	public static Iva findIva(Integer ivaId) {
        if (ivaId == null) return null;
        return entityManager().find(Iva.class, ivaId);
    }

	public static List<Iva> findIvaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Iva o", Iva.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Iva> findIvaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Iva o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Iva.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Iva attached = Iva.findIva(this.ivaId);
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
    public Iva merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Iva merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "linIvaId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<LineaFactura> lineaFacturas;

	@Column(name = "iva_valor")
    @NotNull
    private Integer ivaValor;

	public Set<LineaFactura> getLineaFacturas() {
        return lineaFacturas;
    }

	public void setLineaFacturas(Set<LineaFactura> lineaFacturas) {
        this.lineaFacturas = lineaFacturas;
    }

	public Integer getIvaValor() {
        return ivaValor;
    }

	public void setIvaValor(Integer ivaValor) {
        this.ivaValor = ivaValor;
    }
}
