package com.hamzajg.accounting.selling.infrastructure.persistence;

import com.hamzajg.accounting.selling.infrastructure.*;
import com.hamzajg.accounting.selling.model.client.*;
import com.hamzajg.accounting.selling.infrastructure.Events;

import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class ClientProjectionActor extends StateStoreProjectionActor<ClientData> {

  private static final ClientData Empty = ClientData.empty();

  public ClientProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public ClientProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected ClientData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected ClientData merge(final ClientData previousData, final int previousVersion, final ClientData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    ClientData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case ClientCreated: {
          final ClientCreated typedEvent = typed(event);
          merged = ClientData.from(typedEvent.id, typedEvent.name, typedEvent.activityType);
          break;
        }

        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return merged;
  }
}
