package com.hamzajg.accounting.buying.model.vendor;

import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class VendorEntity extends EventSourced implements Vendor {
  private VendorState state;

  public VendorEntity(final String id) {
    super(id);
    this.state = VendorState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(VendorEntity.class, VendorCreated.class, VendorEntity::applyVendorCreated);
  }

  @Override
  public Completes<VendorState> create(final String name, final String activityType) {
    final VendorState stateArg = state.create(name, activityType);
    return apply(new VendorCreated(stateArg), () -> state);
  }

  private void applyVendorCreated(final VendorCreated event) {
    state = state.create(event.name, event.activityType);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code VendorState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <VendorState> void restoreSnapshot(final VendorState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code VendorState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return VendorState
   */
  @Override
  protected VendorState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
