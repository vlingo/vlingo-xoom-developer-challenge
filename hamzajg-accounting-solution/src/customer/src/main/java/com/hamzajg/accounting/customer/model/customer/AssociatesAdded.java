package com.hamzajg.accounting.customer.model.customer;

import com.hamzajg.accounting.customer.model.Associate;
import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.Set;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 * Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class AssociatesAdded extends IdentifiedDomainEvent {

    public final String id;
    public final Set<Associate> associates;

    public AssociatesAdded(final CustomerState state) {
        super(SemanticVersion.from("1.0.0").toValue());
        this.id = state.id;
        this.associates = state.associates;
    }

    @Override
    public String identity() {
        return id;
    }
}
