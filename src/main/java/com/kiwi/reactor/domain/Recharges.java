package com.kiwi.reactor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Recharges.
 */
@Entity
@Table(name = "recharges")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Recharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_value", nullable = false)
    private Long value;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "disccount", nullable = false)
    private Double disccount;

    @NotNull
    @Column(name = "awarded_secs", nullable = false)
    private Long awardedSecs;

    @OneToOne
    @JoinColumn
    private SuscriberData suscriber;

    @ManyToOne
    private Promotions promotions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public Recharges value(Long value) {
        this.value = value;
        return this;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public Recharges date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getDisccount() {
        return disccount;
    }

    public Recharges disccount(Double disccount) {
        this.disccount = disccount;
        return this;
    }

    public void setDisccount(Double disccount) {
        this.disccount = disccount;
    }

    public Long getAwardedSecs() {
        return awardedSecs;
    }

    public Recharges awardedSecs(Long awardedSecs) {
        this.awardedSecs = awardedSecs;
        return this;
    }

    public void setAwardedSecs(Long awardedSecs) {
        this.awardedSecs = awardedSecs;
    }

    public SuscriberData getSuscriber() {
        return suscriber;
    }

    public Recharges suscriber(SuscriberData suscriberData) {
        this.suscriber = suscriberData;
        return this;
    }

    public void setSuscriber(SuscriberData suscriberData) {
        this.suscriber = suscriberData;
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public Recharges promotions(Promotions promotions) {
        this.promotions = promotions;
        return this;
    }

    public void setPromotions(Promotions promotions) {
        this.promotions = promotions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recharges recharges = (Recharges) o;
        if (recharges.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recharges.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recharges{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", date='" + getDate() + "'" +
            ", disccount='" + getDisccount() + "'" +
            ", awardedSecs='" + getAwardedSecs() + "'" +
            "}";
    }
}
