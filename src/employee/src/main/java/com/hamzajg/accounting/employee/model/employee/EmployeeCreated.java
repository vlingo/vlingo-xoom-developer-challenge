package com.hamzajg.accounting.employee.model.employee;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class EmployeeCreated extends IdentifiedDomainEvent {

  public final String id;

  public EmployeeCreated(final EmployeeState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
  }

  @Override
  public String identity() {
    return id;
  }
}
