package com.hamzajg.accounting.customer.model.customer;

import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import io.vlingo.xoom.symbio.store.object.StateObject;

import java.time.LocalDate;
import java.util.Set;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object Storage</a>
 */
public final class CustomerState extends StateObject {

    public final String id;
    public final String name;
    public final Address address;
    public final LegalStatus legalStatus;
    public final Set<Associate> associates;
    public final String creationDate;
    public final String type;
    public final String activityType;
    public final Capital capital;

    public static CustomerState identifiedBy(final String id) {
        return new CustomerState(id, null, null, null, null, null, null,
                null, null);
    }

    public CustomerState(final String id, final String name, final String type, final String activityType,
                         final String creationDate, final Capital capital, final Address address,
                         final LegalStatus legalStatus, final Set<Associate> associates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.legalStatus = legalStatus;
        this.associates = associates;
        this.creationDate = creationDate;
        this.type = type;
        this.activityType = activityType;
        this.capital = capital;
    }

    public CustomerState create(final String name, final String type, final String activityType, final String creationDate,
                                final Capital capital, final Address address, final LegalStatus legalStatus) {
        return new CustomerState(this.id, name, type, activityType, creationDate, capital, address, legalStatus, this.associates);
    }

    public CustomerState addAssociates(final Set<Associate> associates) {
        return new CustomerState(this.id, this.name, this.type, this.activityType, this.creationDate, this.capital,
                this.address, this.legalStatus, associates);
    }

    public CustomerState removeAssociates(final Set<Associate> associates) {
        return new CustomerState(this.id, this.name, this.type, this.activityType, this.creationDate, this.capital,
                this.address, this.legalStatus, associates);
    }

}
