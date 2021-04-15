package com.hamzajg.accounting.rental.infrastructure.persistence;

import com.hamzajg.accounting.rental.infrastructure.Events;
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContractCreated;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContractTerminated;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 * StateStoreProjectionActor
 * </a>
 */
public class RentalContractProjectionActor extends StateStoreProjectionActor<RentalContractData> {

    private static final RentalContractData Empty = RentalContractData.empty();

    public RentalContractProjectionActor() {
        this(QueryModelStateStoreProvider.instance().store);
    }

    public RentalContractProjectionActor(final StateStore stateStore) {
        super(stateStore);
    }

    @Override
    protected RentalContractData currentDataFor(final Projectable projectable) {
        return Empty;
    }

    @Override
    protected RentalContractData merge(final RentalContractData previousData, final int previousVersion, final RentalContractData currentData, final int currentVersion) {

        if (previousVersion == currentVersion) return currentData;

        RentalContractData merged = previousData;

        for (final Source<?> event : sources()) {
            switch (Events.valueOf(event.typeName())) {
                case RentalContractCreated: {
                    final RentalContractCreated typedEvent = typed(event);
                    merged = RentalContractData.from(typedEvent.id, null, null, null, 0, null, false, null);
                    break;
                }

                case RentalContractTerminated: {
                    final RentalContractTerminated typedEvent = typed(event);
                    merged = RentalContractData.from(typedEvent.id, previousData.startDate, previousData.endDate, previousData.customerId, previousData.paymentPeriod, previousData.price, previousData.isTerminated, typedEvent.terminationDate);
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
