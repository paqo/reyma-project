package com.reyma.gestion.dao;
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "AFECTADO_DOMICILIO_SINIESTRO")
@Configurable
public class AfectadoDomicilioSiniestro {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ads_id")
    private Integer adsId;

	public Integer getAdsId() {
        return this.adsId;
    }

	public void setAdsId(Integer id) {
        this.adsId = id;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("adsDomId", "adsPerId", "adsSinId", "adsTafId").toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new AfectadoDomicilioSiniestro().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAfectadoDomicilioSiniestroes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AfectadoDomicilioSiniestro o", Long.class).getSingleResult();
    }

	public static List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes() {
        return entityManager().createQuery("SELECT o FROM AfectadoDomicilioSiniestro o", AfectadoDomicilioSiniestro.class).getResultList();
    }
	
	public static List<AfectadoDomicilioSiniestro> findAfectadosDomicilioBySiniestro(Integer idSiniestro) {
        return entityManager().createQuery("SELECT o FROM AfectadoDomicilioSiniestro o WHERE o.adsSinId = " + idSiniestro, 
        		AfectadoDomicilioSiniestro.class).getResultList();
    }

	public static List<AfectadoDomicilioSiniestro> findAllAfectadoDomicilioSiniestroes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AfectadoDomicilioSiniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AfectadoDomicilioSiniestro.class).getResultList();
    }

	public static AfectadoDomicilioSiniestro findAfectadoDomicilioSiniestro(Integer adsId) {
        if (adsId == null) return null;
        return entityManager().find(AfectadoDomicilioSiniestro.class, adsId);
    }

	public static List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AfectadoDomicilioSiniestro o", AfectadoDomicilioSiniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AfectadoDomicilioSiniestro> findAfectadoDomicilioSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AfectadoDomicilioSiniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AfectadoDomicilioSiniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AfectadoDomicilioSiniestro attached = AfectadoDomicilioSiniestro.findAfectadoDomicilioSiniestro(this.adsId);
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
    public AfectadoDomicilioSiniestro merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AfectadoDomicilioSiniestro merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@ManyToOne
    @JoinColumn(name = "ads_dom_id", referencedColumnName = "dom_id", nullable = false)
    private Domicilio adsDomId;

	@ManyToOne
    @JoinColumn(name = "ads_per_id", referencedColumnName = "per_id")
    private Persona adsPerId;

	@ManyToOne
    @JoinColumn(name = "ads_sin_id", referencedColumnName = "sin_id", nullable = false)
    private Siniestro adsSinId;

	@ManyToOne
    @JoinColumn(name = "ads_taf_id", referencedColumnName = "taf_id", nullable = false)
    private TipoAfectacion adsTafId;

	public Domicilio getAdsDomId() {
        return adsDomId;
    }

	public void setAdsDomId(Domicilio adsDomId) {
        this.adsDomId = adsDomId;
    }

	public Persona getAdsPerId() {
        return adsPerId;
    }

	public void setAdsPerId(Persona adsPerId) {
        this.adsPerId = adsPerId;
    }

	public Siniestro getAdsSinId() {
        return adsSinId;
    }

	public void setAdsSinId(Siniestro adsSinId) {
        this.adsSinId = adsSinId;
    }

	public TipoAfectacion getAdsTafId() {
        return adsTafId;
    }

	public void setAdsTafId(TipoAfectacion adsTafId) {
        this.adsTafId = adsTafId;
    }
}
