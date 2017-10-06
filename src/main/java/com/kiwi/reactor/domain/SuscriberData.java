package com.kiwi.reactor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SuscriberData.
 */
@Entity
@Table(name = "suscriber_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SuscriberData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fireb_reference", nullable = false)
    private String firebReference;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "balance")
    private Long balance;
    
    @Transient
    private long consumed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirebReference() {
        return firebReference;
    }

    public SuscriberData firebReference(String firebReference) {
        this.firebReference = firebReference;
        return this;
    }

    public void setFirebReference(String firebReference) {
        this.firebReference = firebReference;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SuscriberData phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getBalance() {
        return balance;
    }

    public SuscriberData balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
    
    public long getConsumed() {
		return consumed;
	}

	public void setConsumed(long consumed) {
		this.consumed = consumed;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuscriberData suscriberData = (SuscriberData) o;
        if (suscriberData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suscriberData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SuscriberData{" +
            "id=" + getId() +
            ", firebReference='" + getFirebReference() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", balance='" + getBalance() + "'" +
            "}";
    }
}
