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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "MUNICIPIO")
@Configurable
public class Municipio {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("domicilios", "munPrvId").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mun_id")
    private Integer munId;

	public Integer getMunId() {
        return this.munId;
    }

	public void setMunId(Integer id) {
        this.munId = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Municipio().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMunicipios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Municipio o", Long.class).getSingleResult();
    }

	public static List<Municipio> findAllMunicipios() {
        return entityManager().createQuery("SELECT o FROM Municipio o", Municipio.class).getResultList();
    }

	public static List<Municipio> findAllMunicipios(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Municipio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Municipio.class).getResultList();
    }

	public static Municipio findMunicipio(Integer munId) {
        if (munId == null) return null;
        return entityManager().find(Municipio.class, munId);
    }

	public static List<Municipio> findMunicipioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Municipio o", Municipio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Municipio> findMunicipioEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Municipio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Municipio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Municipio attached = Municipio.findMunicipio(this.munId);
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
    public Municipio merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Municipio merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "domMunId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Domicilio> domicilios;

	@ManyToOne
    @JoinColumn(name = "mun_prv_id", referencedColumnName = "prv_id", nullable = false)
    private Provincia munPrvId;

	@Column(name = "mun_descripcion", length = 255)
    @NotNull
    private String munDescripcion;

	public Set<Domicilio> getDomicilios() {
        return domicilios;
    }

	public void setDomicilios(Set<Domicilio> domicilios) {
        this.domicilios = domicilios;
    }

	public Provincia getMunPrvId() {
        return munPrvId;
    }

	public void setMunPrvId(Provincia munPrvId) {
        this.munPrvId = munPrvId;
    }

	public String getMunDescripcion() {
        return munDescripcion;
    }

	public void setMunDescripcion(String munDescripcion) {
        this.munDescripcion = munDescripcion;
    }
}
