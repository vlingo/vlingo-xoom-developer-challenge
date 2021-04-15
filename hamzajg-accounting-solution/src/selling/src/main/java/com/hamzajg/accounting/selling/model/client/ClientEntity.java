package com.hamzajg.accounting.selling.model.client;

import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class ClientEntity extends EventSourced implements Client {
  private ClientState state;

  public ClientEntity(final String id) {
    super(id);
    this.state = ClientState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(ClientEntity.class, ClientCreated.class, ClientEntity::applyClientCreated);
  }

  @Override
  public Completes<ClientState> create(final String name, final String activityType) {
    final ClientState stateArg = state.create(name, activityType);
    return apply(new ClientCreated(stateArg), () -> state);
  }

  private void applyClientCreated(final ClientCreated event) {
    state = state.create(event.name, event.activityType);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code ClientState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <ClientState> void restoreSnapshot(final ClientState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code ClientState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return ClientState
   */
  @Override
  protected ClientState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
