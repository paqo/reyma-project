package com.reyma.gestion.dao;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "CODIGO_POSTAL")
public class CodigoPostal {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new CodigoPostal().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static List<CodigoPostal> findAll() {
        return entityManager().createQuery("SELECT o FROM Compania o", CodigoPostal.class).getResultList();
    }
	
	public static List<CodigoPostal> findByCodPostal(String cod) {
		return entityManager().createQuery("SELECT cp FROM CodigoPostal cp WHERE cp.cpCodigo = '" + cod + "'", 
				CodigoPostal.class).getResultList();
	}

	public static CodigoPostal findCP(Integer comId) {
        if (comId == null) return null;
        return entityManager().find(CodigoPostal.class, comId);
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
            CodigoPostal attached = CodigoPostal.findCP(this.cpId);
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
    public CodigoPostal merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CodigoPostal merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Column(name = "cp_municipio", length = 80)
    @NotNull
    private String cpMunicipio;

	@Column(name = "cp_codigo", length = 6)
    @NotNull
    private String cpCodigo;

	public String getCpMunicipio() {
		return cpMunicipio;
	}

	public void setCpMunicipio(String cpMunicipio) {
		this.cpMunicipio = cpMunicipio;
	}

	public String getCpCodigo() {
		return cpCodigo;
	}

	public void setCpCodigo(String cpCodigo) {
		this.cpCodigo = cpCodigo;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cp_id")
    private Integer cpId;

	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
