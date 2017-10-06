package com.kiwi.reactor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Promotions.
 */
@Entity
@Table(name = "promotions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Promotions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "configuration_date", nullable = false)
    private LocalDate configurationDate;

    @NotNull
    @Column(name = "test", nullable = false)
    private Boolean test;

    @NotNull
    @Column(name = "module", nullable = false)
    private String module;

    @NotNull
    @Column(name = "application", nullable = false)
    private LocalDate application;

    @OneToMany(mappedBy = "promotions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Recharges> proms = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getConfigurationDate() {
        return configurationDate;
    }

    public Promotions configurationDate(LocalDate configurationDate) {
        this.configurationDate = configurationDate;
        return this;
    }

    public void setConfigurationDate(LocalDate configurationDate) {
        this.configurationDate = configurationDate;
    }

    public Boolean isTest() {
        return test;
    }

    public Promotions test(Boolean test) {
        this.test = test;
        return this;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public String getModule() {
        return module;
    }

    public Promotions module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public LocalDate getApplication() {
        return application;
    }

    public Promotions application(LocalDate application) {
        this.application = application;
        return this;
    }

    public void setApplication(LocalDate application) {
        this.application = application;
    }

    public Set<Recharges> getProms() {
        return proms;
    }

    public Promotions proms(Set<Recharges> recharges) {
        this.proms = recharges;
        return this;
    }

    public Promotions addProm(Recharges recharges) {
        this.proms.add(recharges);
        recharges.setPromotions(this);
        return this;
    }

    public Promotions removeProm(Recharges recharges) {
        this.proms.remove(recharges);
        recharges.setPromotions(null);
        return this;
    }

    public void setProms(Set<Recharges> recharges) {
        this.proms = recharges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Promotions promotions = (Promotions) o;
        if (promotions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), promotions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Promotions{" +
            "id=" + getId() +
            ", configurationDate='" + getConfigurationDate() + "'" +
            ", test='" + isTest() + "'" +
            ", module='" + getModule() + "'" +
            ", application='" + getApplication() + "'" +
            "}";
    }
}
