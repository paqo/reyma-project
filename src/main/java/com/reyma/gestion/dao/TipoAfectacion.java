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
@Table(name = "TIPO_AFECTACION")
public class TipoAfectacion {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("afectadoDomicilioSiniestroes").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "taf_id")
    private Integer tafId;

	public Integer getTafId() {
        return this.tafId;
    }

	public void setTafId(Integer id) {
        this.tafId = id;
    }
	
	@OneToMany(mappedBy = "adsTafId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes;

	@Column(name = "taf_descripcion", length = 50)
    @NotNull
    private String tafDescripcion;

	public Set<AfectadoDomicilioSiniestro> getAfectadoDomicilioSiniestroes() {
        return afectadoDomicilioSiniestroes;
    }

	public void setAfectadoDomicilioSiniestroes(Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes) {
        this.afectadoDomicilioSiniestroes = afectadoDomicilioSiniestroes;
    }

	public String getTafDescripcion() {
        return tafDescripcion;
    }

	public void setTafDescripcion(String tafDescripcion) {
        this.tafDescripcion = tafDescripcion;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new TipoAfectacion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTipoAfectacions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TipoAfectacion o", Long.class).getSingleResult();
    }

	public static List<TipoAfectacion> findAllTipoAfectacions() {
        return entityManager().createQuery("SELECT o FROM TipoAfectacion o", TipoAfectacion.class).getResultList();
    }

	public static List<TipoAfectacion> findAllTipoAfectacions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TipoAfectacion o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TipoAfectacion.class).getResultList();
    }

	public static TipoAfectacion findTipoAfectacion(Integer tafId) {
        if (tafId == null) return null;
        return entityManager().find(TipoAfectacion.class, tafId);
    }

	public static List<TipoAfectacion> findTipoAfectacionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TipoAfectacion o", TipoAfectacion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TipoAfectacion> findTipoAfectacionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TipoAfectacion o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TipoAfectacion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TipoAfectacion attached = TipoAfectacion.findTipoAfectacion(this.tafId);
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
    public TipoAfectacion merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TipoAfectacion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
