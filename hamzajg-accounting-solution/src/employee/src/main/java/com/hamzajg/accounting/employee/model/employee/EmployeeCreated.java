package com.hamzajg.accounting.employee.model.employee;

import com.hamzajg.accounting.employee.model.Address;
import com.hamzajg.accounting.employee.model.FullName;
import com.hamzajg.accounting.employee.model.Money;
import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 * Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class EmployeeCreated extends IdentifiedDomainEvent {

    public final String id;
    public final String exerciseId;
    public final FullName fullName;
    public final Address address;
    public final double workingPeriod;
    public final Money cost;

    public EmployeeCreated(final EmployeeState state) {
        super(SemanticVersion.from("1.0.0").toValue());
        this.id = state.id;
        this.exerciseId = state.exerciseId;
        this.fullName = state.fullName;
        this.address = state.address;
        this.workingPeriod = state.workingPeriod;
        this.cost = state.cost;
    }

    @Override
    public String identity() {
        return id;
    }
}
