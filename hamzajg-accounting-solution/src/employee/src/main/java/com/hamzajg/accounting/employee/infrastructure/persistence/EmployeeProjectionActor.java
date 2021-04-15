package com.hamzajg.accounting.employee.infrastructure.persistence;

import com.hamzajg.accounting.employee.infrastructure.Events;
import com.hamzajg.accounting.employee.infrastructure.*;
import com.hamzajg.accounting.employee.model.employee.*;

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
public class EmployeeProjectionActor extends StateStoreProjectionActor<EmployeeData> {

  private static final EmployeeData Empty = EmployeeData.empty();

  public EmployeeProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public EmployeeProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected EmployeeData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected EmployeeData merge(final EmployeeData previousData, final int previousVersion, final EmployeeData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    EmployeeData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case EmployeeCreated: {
          final EmployeeCreated typedEvent = typed(event);
          merged = EmployeeData.from(typedEvent.id, null, null, 0, null);
          break;
        }

        case EmployeeWorkingPeriodChanged: {
          final EmployeeWorkingPeriodChanged typedEvent = typed(event);
          merged = EmployeeData.from(typedEvent.id, previousData.fullName, previousData.address, typedEvent.workingPeriod, previousData.cost);
          break;
        }

        case EmployeeCostChanged: {
          final EmployeeCostChanged typedEvent = typed(event);
          final MoneyData cost = MoneyData.from(typedEvent.cost.amount, typedEvent.cost.currency);
          merged = EmployeeData.from(typedEvent.id, previousData.fullName, previousData.address, previousData.workingPeriod, cost);
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
