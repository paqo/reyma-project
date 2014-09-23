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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "PERSONA")
@Configurable
public class Persona {

	@OneToMany(mappedBy = "adsPerId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes;

	@Column(name = "per_nombre", length = 80)
    private String perNombre;

	@Column(name = "per_nif", length = 10)
    private String perNif;

	@Column(name = "per_tlf1", length = 25)
    private String perTlf1;

	@Column(name = "per_tlf2", length = 25)
    private String perTlf2;

	public Set<AfectadoDomicilioSiniestro> getAfectadoDomicilioSiniestroes() {
        return afectadoDomicilioSiniestroes;
    }

	public void setAfectadoDomicilioSiniestroes(Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes) {
        this.afectadoDomicilioSiniestroes = afectadoDomicilioSiniestroes;
    }

	public String getPerNombre() {
        return perNombre;
    }

	public void setPerNombre(String perNombre) {
        this.perNombre = perNombre;
    }

	public String getPerNif() {
        return perNif;
    }

	public void setPerNif(String perNif) {
        this.perNif = perNif;
    }

	public String getPerTlf1() {
        return perTlf1;
    }

	public void setPerTlf1(String perTlf1) {
        this.perTlf1 = perTlf1;
    }

	public String getPerTlf2() {
        return perTlf2;
    }

	public void setPerTlf2(String perTlf2) {
        this.perTlf2 = perTlf2;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "per_id")
    private Integer perId;

	public Integer getPerId() {
        return this.perId;
    }

	public void setPerId(Integer id) {
        this.perId = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Persona().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPersonae() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Persona o", Long.class).getSingleResult();
    }

	public static List<Persona> findAllPersonae() {
        return entityManager().createQuery("SELECT o FROM Persona o", Persona.class).getResultList();
    }

	public static List<Persona> findAllPersonae(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Persona o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Persona.class).getResultList();
    }

	public static Persona findPersona(Integer perId) {
        if (perId == null) return null;
        return entityManager().find(Persona.class, perId);
    }

	public static List<Persona> findPersonaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Persona o", Persona.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Persona> findPersonaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Persona o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Persona.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Persona attached = Persona.findPersona(this.perId);
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
    public Persona merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Persona merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("afectadoDomicilioSiniestroes").toString();
    }
}
