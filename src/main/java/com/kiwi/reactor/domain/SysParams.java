package com.kiwi.reactor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SysParams.
 */
@Entity
@Table(name = "sys_params")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SysParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "s_value", nullable = false)
    private String sValue;

    @NotNull
    @Column(name = "n_value", nullable = false)
    private Double nValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SysParams name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsValue() {
        return sValue;
    }

    public SysParams sValue(String sValue) {
        this.sValue = sValue;
        return this;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }

    public Double getnValue() {
        return nValue;
    }

    public SysParams nValue(Double nValue) {
        this.nValue = nValue;
        return this;
    }

    public void setnValue(Double nValue) {
        this.nValue = nValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysParams sysParams = (SysParams) o;
        if (sysParams.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sysParams.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SysParams{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sValue='" + getsValue() + "'" +
            ", nValue='" + getnValue() + "'" +
            "}";
    }
}
