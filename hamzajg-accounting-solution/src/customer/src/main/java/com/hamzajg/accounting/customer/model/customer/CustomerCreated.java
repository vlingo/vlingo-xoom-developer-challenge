package com.hamzajg.accounting.customer.model.customer;

import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.Set;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 * Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class CustomerCreated extends IdentifiedDomainEvent {

    public final String id;
    public final String name;
    public final Address address;
    public final LegalStatus legalStatus;
    public final Set<Associate> associates;
    public final String creationDate;
    public final String type;
    public final String activityType;
    public final Capital capital;

    public CustomerCreated(final CustomerState state) {
        super(SemanticVersion.from("1.0.0").toValue());
        this.id = state.id;
        this.name = state.name;
        this.address = state.address;
        this.legalStatus = state.legalStatus;
        this.associates = state.associates;
        this.creationDate = state.creationDate;
        this.type = state.type;
        this.activityType = state.activityType;
        this.capital = state.capital;
    }

    @Override
    public String identity() {
        return id;
    }
}
