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
@Table(name = "TIPO_SINIESTRO")
@Configurable
public class TipoSiniestro {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tsi_id")
    private Integer tsiId;

	public Integer getTsiId() {
        return this.tsiId;
    }

	public void setTsiId(Integer id) {
        this.tsiId = id;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("siniestroes").toString();
    }

	@OneToMany(mappedBy = "sinTsiId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Siniestro> siniestroes;

	@Column(name = "tsi_descripcion", length = 30)
    @NotNull
    private String tsiDescripcion;

	public Set<Siniestro> getSiniestroes() {
        return siniestroes;
    }

	public void setSiniestroes(Set<Siniestro> siniestroes) {
        this.siniestroes = siniestroes;
    }

	public String getTsiDescripcion() {
        return tsiDescripcion;
    }

	public void setTsiDescripcion(String tsiDescripcion) {
        this.tsiDescripcion = tsiDescripcion;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new TipoSiniestro().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTipoSiniestroes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TipoSiniestro o", Long.class).getSingleResult();
    }

	public static List<TipoSiniestro> findAllTipoSiniestroes() {
        return entityManager().createQuery("SELECT o FROM TipoSiniestro o", TipoSiniestro.class).getResultList();
    }

	public static List<TipoSiniestro> findAllTipoSiniestroes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TipoSiniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TipoSiniestro.class).getResultList();
    }

	public static TipoSiniestro findTipoSiniestro(Integer tsiId) {
        if (tsiId == null) return null;
        return entityManager().find(TipoSiniestro.class, tsiId);
    }

	public static List<TipoSiniestro> findTipoSiniestroEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TipoSiniestro o", TipoSiniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TipoSiniestro> findTipoSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TipoSiniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TipoSiniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TipoSiniestro attached = TipoSiniestro.findTipoSiniestro(this.tsiId);
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
    public TipoSiniestro merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TipoSiniestro merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
