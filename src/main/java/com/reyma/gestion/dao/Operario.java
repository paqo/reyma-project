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
@Table(name = "OPERARIO")
public class Operario {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("trabajoes").toString();
    }

	@OneToMany(mappedBy = "traOpeId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Trabajo> trabajoes;

	@Column(name = "ope_nombre_pila", length = 20)
    @NotNull
    private String opeNombrePila;

	@Column(name = "ope_nombre", length = 50)
    private String opeNombre;

	public Set<Trabajo> getTrabajoes() {
        return trabajoes;
    }

	public void setTrabajoes(Set<Trabajo> trabajoes) {
        this.trabajoes = trabajoes;
    }

	public String getOpeNombrePila() {
        return opeNombrePila;
    }

	public void setOpeNombrePila(String opeNombrePila) {
        this.opeNombrePila = opeNombrePila;
    }

	public String getOpeNombre() {
        return opeNombre;
    }

	public void setOpeNombre(String opeNombre) {
        this.opeNombre = opeNombre;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Operario().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOperarios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Operario o", Long.class).getSingleResult();
    }

	public static List<Operario> findAllOperarios() {
        return entityManager().createQuery("SELECT o FROM Operario o", Operario.class).getResultList();
    }

	public static List<Operario> findAllOperarios(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Operario o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Operario.class).getResultList();
    }

	public static Operario findOperario(Integer opeId) {
        if (opeId == null) return null;
        return entityManager().find(Operario.class, opeId);
    }

	public static List<Operario> findOperarioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Operario o", Operario.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Operario> findOperarioEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Operario o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Operario.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Operario attached = Operario.findOperario(this.opeId);
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
    public Operario merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Operario merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ope_id")
    private Integer opeId;

	public Integer getOpeId() {
        return this.opeId;
    }

	public void setOpeId(Integer id) {
        this.opeId = id;
    }
}
