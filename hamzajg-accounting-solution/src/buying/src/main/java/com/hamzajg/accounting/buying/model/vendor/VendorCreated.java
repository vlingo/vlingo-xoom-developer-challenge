package com.hamzajg.accounting.buying.model.vendor;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class VendorCreated extends IdentifiedDomainEvent {

  public final String id;
  public final String name;
  public final String activityType;

  public VendorCreated(final VendorState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.name = state.name;
    this.activityType = state.activityType;
  }

  @Override
  public String identity() {
    return id;
  }
}
