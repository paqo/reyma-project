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
@Table(name = "COMPANIA")
public class Compania {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Compania().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCompanias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Compania o", Long.class).getSingleResult();
    }

	public static List<Compania> findAllCompanias() {
        return entityManager().createQuery("SELECT o FROM Compania o", Compania.class).getResultList();
    }

	public static List<Compania> findAllCompanias(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compania o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compania.class).getResultList();
    }

	public static Compania findCompania(Integer comId) {
        if (comId == null) return null;
        return entityManager().find(Compania.class, comId);
    }

	public static List<Compania> findCompaniaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Compania o", Compania.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Compania> findCompaniaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compania o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compania.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Compania attached = Compania.findCompania(this.comId);
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
    public Compania merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Compania merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "sinComId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Siniestro> siniestroes;

	@Column(name = "com_descripcion", length = 50)
    @NotNull
    private String comDescripcion;

	@Column(name = "com_codigo", length = 3)
    @NotNull
    private String comCodigo;

	public Set<Siniestro> getSiniestroes() {
        return siniestroes;
    }

	public void setSiniestroes(Set<Siniestro> siniestroes) {
        this.siniestroes = siniestroes;
    }

	public String getComDescripcion() {
        return comDescripcion;
    }

	public void setComDescripcion(String comDescripcion) {
        this.comDescripcion = comDescripcion;
    }

	public String getComCodigo() {
        return comCodigo;
    }

	public void setComCodigo(String comCodigo) {
        this.comCodigo = comCodigo;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "com_id")
    private Integer comId;

	public Integer getComId() {
        return this.comId;
    }

	public void setComId(Integer id) {
        this.comId = id;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("siniestroes").toString();
    }

	public static Compania findCompaniaByDesc(String companiaDesc) {
		return entityManager().createQuery(
				"SELECT o FROM Compania o WHERE o.comCodigo = '" + companiaDesc + "'", 
				Compania.class).
				getSingleResult();
	}
}
