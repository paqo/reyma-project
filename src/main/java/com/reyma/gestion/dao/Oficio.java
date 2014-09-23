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
@Table(name = "OFICIO")
public class Oficio {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Oficio().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOficios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Oficio o", Long.class).getSingleResult();
    }

	public static List<Oficio> findAllOficios() {
        return entityManager().createQuery("SELECT o FROM Oficio o", Oficio.class).getResultList();
    }

	public static List<Oficio> findAllOficios(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Oficio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Oficio.class).getResultList();
    }

	public static Oficio findOficio(Integer ofiId) {
        if (ofiId == null) return null;
        return entityManager().find(Oficio.class, ofiId);
    }

	public static List<Oficio> findOficioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Oficio o", Oficio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Oficio> findOficioEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Oficio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Oficio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Oficio attached = Oficio.findOficio(this.ofiId);
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
    public Oficio merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Oficio merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ofi_id")
    private Integer ofiId;

	public Integer getOfiId() {
        return this.ofiId;
    }

	public void setOfiId(Integer id) {
        this.ofiId = id;
    }

	@OneToMany(mappedBy = "traOfiId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Trabajo> trabajoes;

	@Column(name = "ofi_descripcion", length = 50)
    @NotNull
    private String ofiDescripcion;

	public Set<Trabajo> getTrabajoes() {
        return trabajoes;
    }

	public void setTrabajoes(Set<Trabajo> trabajoes) {
        this.trabajoes = trabajoes;
    }

	public String getOfiDescripcion() {
        return ofiDescripcion;
    }

	public void setOfiDescripcion(String ofiDescripcion) {
        this.ofiDescripcion = ofiDescripcion;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("trabajoes").toString();
    }
}
