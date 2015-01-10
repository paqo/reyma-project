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
@Table(name = "PROVINCIA")
@Configurable
public class Provincia {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Provincia().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProvincias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Provincia o", Long.class).getSingleResult();
    }

	public static List<Provincia> findAllProvincias() {
        return entityManager().createQuery("SELECT o FROM Provincia o", Provincia.class).getResultList();
    }

	public static List<Provincia> findAllProvincias(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Provincia o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Provincia.class).getResultList();
    }

	public static Provincia findProvincia(Integer prvId) {
        if (prvId == null) return null;
        return entityManager().find(Provincia.class, prvId);
    }

	public static List<Provincia> findProvinciaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Provincia o", Provincia.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Provincia> findProvinciaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Provincia o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Provincia.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Provincia attached = Provincia.findProvincia(this.prvId);
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
    public Provincia merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Provincia merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("domicilios", "municipios").toString();
    }

	@OneToMany(mappedBy = "domProvId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Domicilio> domicilios;

	@OneToMany(mappedBy = "munPrvId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Municipio> municipios;

	@Column(name = "prv_descripcion", length = 80)
    @NotNull
    private String prvDescripcion;

	public Set<Domicilio> getDomicilios() {
        return domicilios;
    }

	public void setDomicilios(Set<Domicilio> domicilios) {
        this.domicilios = domicilios;
    }

	public Set<Municipio> getMunicipios() {
        return municipios;
    }

	public void setMunicipios(Set<Municipio> municipios) {
        this.municipios = municipios;
    }

	public String getPrvDescripcion() {
        return prvDescripcion;
    }

	public void setPrvDescripcion(String prvDescripcion) {
        this.prvDescripcion = prvDescripcion;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prv_id")
    private Integer prvId;

	public Integer getPrvId() {
        return this.prvId;
    }

	public void setPrvId(Integer id) {
        this.prvId = id;
    }

	public static Provincia findProvinciaByDescripcion(String desc, boolean sensitive) {
		String condicion = sensitive? "WHERE UPPER(o.prvDescripcion) = '" + desc.toUpperCase() + "'" : 
									  "WHERE o.prvDescripcion = '" + desc + "'";
		
		String jpaQuery = "SELECT o FROM Provincia o " + condicion;       
        return entityManager().createQuery(jpaQuery, Provincia.class).getSingleResult();
	}
}
