package com.hamzajg.accounting.customer.model.exercise;


import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.time.LocalDate;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 * Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class ExerciseEndDateChanged extends IdentifiedDomainEvent {

    public final String id;
    public final String endDate;

    public ExerciseEndDateChanged(final ExerciseState state) {
        super(SemanticVersion.from("1.0.0").toValue());
        this.id = state.id;
        this.endDate = state.endDate;
    }

    @Override
    public String identity() {
        return id;
    }
}
