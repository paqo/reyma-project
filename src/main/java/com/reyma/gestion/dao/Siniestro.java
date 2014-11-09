package com.reyma.gestion.dao;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.reyma.gestion.util.Fechas;

@Configurable
@Entity
@Table(name = "SINIESTRO")
public class Siniestro {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("sinTsiId", "sinFechaOcurrencia");

	public static final EntityManager entityManager() {
        EntityManager em = new Siniestro().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSiniestroes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Siniestro o", Long.class).getSingleResult();
    }

	public static List<Siniestro> findAllSiniestroes() {
        return entityManager().createQuery("SELECT o FROM Siniestro o", Siniestro.class).getResultList();
    }

	public static List<Siniestro> findAllSiniestroes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Siniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        
        // return entityManager().createNativeQuery("SELECT * FROM SINIESTRO order by sin_fecha_comunicacion desc", Siniestro.class).getResultList();
        
        return entityManager().createQuery(jpaQuery, Siniestro.class).getResultList();
    }

	public static Siniestro findSiniestro(Integer sinId) {
        if (sinId == null) return null;
        return entityManager().find(Siniestro.class, sinId);
    }

	public static List<Siniestro> findSiniestroEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Siniestro o", Siniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Siniestro> findSiniestroEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Siniestro o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Siniestro.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Siniestro attached = Siniestro.findSiniestro(this.sinId);
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
    public Siniestro merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Siniestro merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("afectadoDomicilioSiniestroes", "facturas", "trabajoes", "sinComId", "sinEstId", "sinTsiId").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sin_id")
    private Integer sinId;

	public Integer getSinId() {
        return this.sinId;
    }

	public void setSinId(Integer id) {
        this.sinId = id;
    }

	@OneToMany(mappedBy = "adsSinId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes;

	@OneToMany(mappedBy = "facSinId")
    private Set<Factura> facturas;

	@OneToMany(mappedBy = "traSinId", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Trabajo> trabajoes;

	@ManyToOne
    @JoinColumn(name = "sin_com_id", referencedColumnName = "com_id")
    private Compania sinComId;

	@ManyToOne
    @JoinColumn(name = "sin_est_id", referencedColumnName = "est_id")
    private Estado sinEstId;

	@ManyToOne
    @JoinColumn(name = "sin_tsi_id", referencedColumnName = "tsi_id")
    private TipoSiniestro sinTsiId;

	@Column(name = "sin_numero", length = 50, unique = true)
    @NotNull
    private String sinNumero;

	@Column(name = "sin_poliza", length = 50)
    private String sinPoliza;

	@Column(name = "sin_fecha_comunicacion")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar sinFechaComunicacion;

	@Column(name = "sin_fecha_ocurrencia")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat( pattern = Fechas.FORMATO_FECHA_DDMMYYYYHHMM)
    private Calendar sinFechaOcurrencia;

	@Column(name = "sin_descripcion", length = 500)
    private String sinDescripcion;

	@Column(name = "sin_observaciones", length = 250)
    private String sinObservaciones;

	@Column(name = "sin_urgente")
    private Short sinUrgente;

	public Set<AfectadoDomicilioSiniestro> getAfectadoDomicilioSiniestroes() {
        return afectadoDomicilioSiniestroes;
    }

	public void setAfectadoDomicilioSiniestroes(Set<AfectadoDomicilioSiniestro> afectadoDomicilioSiniestroes) {
        this.afectadoDomicilioSiniestroes = afectadoDomicilioSiniestroes;
    }

	public Set<Factura> getFacturas() {
        return facturas;
    }

	public void setFacturas(Set<Factura> facturas) {
        this.facturas = facturas;
    }

	public Set<Trabajo> getTrabajoes() {
        return trabajoes;
    }

	public void setTrabajoes(Set<Trabajo> trabajoes) {
        this.trabajoes = trabajoes;
    }

	public Compania getSinComId() {
        return sinComId;
    }

	public void setSinComId(Compania sinComId) {
        this.sinComId = sinComId;
    }

	public Estado getSinEstId() {
        return sinEstId;
    }

	public void setSinEstId(Estado sinEstId) {
        this.sinEstId = sinEstId;
    }

	public TipoSiniestro getSinTsiId() {
        return sinTsiId;
    }

	public void setSinTsiId(TipoSiniestro sinTsiId) {
        this.sinTsiId = sinTsiId;
    }

	public String getSinNumero() {
        return sinNumero;
    }

	public void setSinNumero(String sinNumero) {
        this.sinNumero = sinNumero;
    }

	public String getSinPoliza() {
        return sinPoliza;
    }

	public void setSinPoliza(String sinPoliza) {
        this.sinPoliza = sinPoliza;
    }

	public Calendar getSinFechaComunicacion() {
        return sinFechaComunicacion;
    }

	public void setSinFechaComunicacion(Calendar sinFechaComunicacion) {
        this.sinFechaComunicacion = sinFechaComunicacion;
    }

	public Calendar getSinFechaOcurrencia() {
        return sinFechaOcurrencia;
    }

	public void setSinFechaOcurrencia(Calendar sinFechaOcurrencia) {
        this.sinFechaOcurrencia = sinFechaOcurrencia;
    }

	public String getSinDescripcion() {
        return sinDescripcion;
    }

	public void setSinDescripcion(String sinDescripcion) {
        this.sinDescripcion = sinDescripcion;
    }

	public String getSinObservaciones() {
        return sinObservaciones;
    }

	public void setSinObservaciones(String sinObservaciones) {
        this.sinObservaciones = sinObservaciones;
    }

	public Short getSinUrgente() {
        return sinUrgente;
    }

	public void setSinUrgente(Short sinUrgente) {
        this.sinUrgente = sinUrgente;
    }
}
