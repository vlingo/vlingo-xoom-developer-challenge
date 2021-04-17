package com.hamzajg.accounting.purchase.infrastructure.persistence;

import com.hamzajg.accounting.purchase.model.vendor.*;
import com.hamzajg.accounting.purchase.infrastructure.Events;
import com.hamzajg.accounting.purchase.infrastructure.*;

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
public class VendorProjectionActor extends StateStoreProjectionActor<VendorData> {

  private static final VendorData Empty = VendorData.empty();

  public VendorProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public VendorProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected VendorData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected VendorData merge(final VendorData previousData, final int previousVersion, final VendorData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    VendorData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case VendorCreated: {
          final VendorCreated typedEvent = typed(event);
          merged = VendorData.from(typedEvent.id, typedEvent.name, typedEvent.activityType);
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
