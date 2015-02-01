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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "DOMICILIO")
public class Domicilio {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dom_id")
    private Integer domId;

	public Integer getDomId() {
        return this.domId;
    }

	public void setDomId(Integer id) {
        this.domId = id;
    }

	private static Logger logger = Logger.getLogger(Domicilio.class);
	
	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("afectadoDomicilioSiniestroes", "domProvId", "domMunId").toString();
    }

	@OneToMany(mappedBy = "adsDomId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes;

	@ManyToOne
    @JoinColumn(name = "dom_prov_id", referencedColumnName = "prv_id", nullable = false)
    private Provincia domProvId;

	@ManyToOne
    @JoinColumn(name = "dom_mun_id", referencedColumnName = "mun_id", nullable = true)
    private Municipio domMunId;

	@Column(name = "dom_direccion", length = 80)
    @NotNull
    private String domDireccion;

	@Column(name = "dom_cp")
    private Integer domCp;

	public Set<AfectadoDomicilioSiniestro> getAfectadoDomicilioSiniestroes() {
        return afectadoDomicilioSiniestroes;
    }

	public void setAfectadoDomicilioSiniestroes(Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes) {
        this.afectadoDomicilioSiniestroes = afectadoDomicilioSiniestroes;
    }

	public Provincia getDomProvId() {
        return domProvId;
    }

	public void setDomProvId(Provincia domProvId) {
        this.domProvId = domProvId;
    }

	public Municipio getDomMunId() {
        return domMunId;
    }

	public void setDomMunId(Municipio domMunId) {
        this.domMunId = domMunId;
    }

	public String getDomDireccion() {
        return domDireccion;
    }

	public void setDomDireccion(String domDireccion) {
        this.domDireccion = domDireccion;
    }

	public Integer getDomCp() {
        return domCp;
    }

	public void setDomCp(Integer domCp) {
        this.domCp = domCp;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Domicilio().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDomicilios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Domicilio o", Long.class).getSingleResult();
    }

	public static List<Domicilio> findAllDomicilios() {
        return entityManager().createQuery("SELECT o FROM Domicilio o", Domicilio.class).getResultList();
    }

	public static List<Domicilio> findAllDomicilios(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Domicilio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Domicilio.class).getResultList();
    }

	public static Domicilio findDomicilio(Integer domId) {
        if (domId == null) return null;
        return entityManager().find(Domicilio.class, domId);
    }

	public static List<Domicilio> findDomicilioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Domicilio o", Domicilio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Domicilio> findDomicilioEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Domicilio o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Domicilio.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Domicilio attached = Domicilio.findDomicilio(this.domId);
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
    public Domicilio merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Domicilio merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Domicilio findDomicilio(Domicilio domicilio) {		
		Domicilio domicilioEnc = null;
		if ( domicilio != null ){ 
			try {
				domicilioEnc = entityManager().createQuery("SELECT o FROM Domicilio o "
						+ "WHERE o.domDireccion = :direccion "
						+ "AND o.domMunId = :municipio "
						+ "AND o.domProvId = :provincia", Domicilio.class)
						.setParameter("direccion", domicilio.getDomDireccion())
						.setParameter("municipio", domicilio.getDomMunId())
						.setParameter("provincia", domicilio.getDomProvId())
						.getSingleResult();
			} catch (EmptyResultDataAccessException e){
				logger.debug("domicilio no encontrado: " + domicilio);
				return null;
			} catch (IncorrectResultSizeDataAccessException e) {		
				logger.debug("encontrado mas de un domicilio para criterio de busqueda: " + domicilio);
				return null;
			}	
		}		
		return domicilioEnc;
	}
}
