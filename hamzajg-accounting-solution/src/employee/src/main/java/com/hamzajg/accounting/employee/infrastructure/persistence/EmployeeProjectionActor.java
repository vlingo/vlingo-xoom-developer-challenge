package com.hamzajg.accounting.employee.infrastructure.persistence;

import com.hamzajg.accounting.employee.infrastructure.*;
import com.hamzajg.accounting.employee.model.employee.EmployeeCostChanged;
import com.hamzajg.accounting.employee.model.employee.EmployeeCreated;
import com.hamzajg.accounting.employee.model.employee.EmployeeWorkingPeriodChanged;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See <a href=
 * "https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 * StateStoreProjectionActor </a>
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
    protected EmployeeData merge(final EmployeeData previousData, final int previousVersion,
                                 final EmployeeData currentData, final int currentVersion) {

        if (previousVersion == currentVersion)
            return currentData;

        EmployeeData merged = previousData;

        for (final Source<?> event : sources()) {
            switch (Events.valueOf(event.typeName())) {
                case EmployeeCreated: {
                    final EmployeeCreated typedEvent = typed(event);
                    final FullNameData fullName = FullNameData.from(typedEvent.fullName.firstName, typedEvent.fullName.secondName, typedEvent.fullName.lastName);
                    final MoneyData cost = MoneyData.from(typedEvent.cost.amount, typedEvent.cost.currency);
                    final AddressData address = AddressData.from(typedEvent.address.firstLine, typedEvent.address.secondLine);
                    merged = EmployeeData.from(typedEvent.id, typedEvent.exerciseId, fullName, address, typedEvent.workingPeriod, cost);
                    break;
                }

                case EmployeeWorkingPeriodChanged: {
                    final EmployeeWorkingPeriodChanged typedEvent = typed(event);
                    merged = EmployeeData.from(typedEvent.id, previousData.exerciseId, previousData.fullName, previousData.address,
                            typedEvent.workingPeriod, previousData.cost);
                    break;
                }

                case EmployeeCostChanged: {
                    final EmployeeCostChanged typedEvent = typed(event);
                    final MoneyData cost = MoneyData.from(typedEvent.cost.amount, typedEvent.cost.currency);
                    merged = EmployeeData.from(typedEvent.id, previousData.exerciseId, previousData.fullName, previousData.address,
                            previousData.workingPeriod, cost);
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
