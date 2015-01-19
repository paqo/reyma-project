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

@Configurable
@Entity
@Table(name = "ESTADO")
public class Estado {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Estado().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countEstadoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Estado o", Long.class).getSingleResult();
    }

	public static List<Estado> findAllEstadoes() {
        return entityManager().createQuery("SELECT o FROM Estado o", Estado.class).getResultList();
    }

	public static List<Estado> findAllEstadoes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Estado o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Estado.class).getResultList();
    }

	public static Estado findEstado(Integer estId) {
        if (estId == null) return null;
        return entityManager().find(Estado.class, estId);
    }
	
	public static Estado findEstadoByDescripcion(String descripcion) {
        if (descripcion == null) return null;
        return entityManager().createQuery("SELECT o FROM Estado o "
				+ "WHERE o.estDescripcion = :desc", Estado.class)
				.setParameter("desc", descripcion)
				.getSingleResult();
    }

	public static List<Estado> findEstadoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Estado o", Estado.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Estado> findEstadoEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Estado o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Estado.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Estado attached = Estado.findEstado(this.estId);
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
    public Estado merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Estado merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("siniestroes").toString();
    }

	@OneToMany(mappedBy = "sinEstId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Siniestro> siniestroes;

	@Column(name = "est_descripcion", length = 20)
    @NotNull
    private String estDescripcion;

	public Set<Siniestro> getSiniestroes() {
        return siniestroes;
    }

	public void setSiniestroes(Set<Siniestro> siniestroes) {
        this.siniestroes = siniestroes;
    }

	public String getEstDescripcion() {
        return estDescripcion;
    }

	public void setEstDescripcion(String estDescripcion) {
        this.estDescripcion = estDescripcion;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "est_id")
    private Integer estId;

	public Integer getEstId() {
        return this.estId;
    }

	public void setEstId(Integer id) {
        this.estId = id;
    }
}
