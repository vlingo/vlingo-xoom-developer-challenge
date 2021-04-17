package com.hamzajg.accounting.customer.model.exercise;


import com.hamzajg.accounting.customer.model.customer.CustomerState;
import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 * Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class ExerciseCreated extends IdentifiedDomainEvent {

    public final String id;
    public final String startDate;
    public final String endDate;
    public final String customerId;

    public ExerciseCreated(final ExerciseState state) {
        super(SemanticVersion.from("1.0.0").toValue());
        this.id = state.id;
        this.startDate = state.startDate;
        this.endDate = state.endDate;
        this.customerId = state.customerId;
    }

    @Override
    public String identity() {
        return id;
    }
}
