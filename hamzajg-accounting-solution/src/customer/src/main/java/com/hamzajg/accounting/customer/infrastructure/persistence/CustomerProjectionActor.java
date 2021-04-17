package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.*;
import com.hamzajg.accounting.customer.model.customer.AssociatesAdded;
import com.hamzajg.accounting.customer.model.customer.AssociatesRemoved;
import com.hamzajg.accounting.customer.model.customer.CustomerCreated;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 * StateStoreProjectionActor
 * </a>
 */
public class CustomerProjectionActor extends StateStoreProjectionActor<CustomerData> {

    private static final CustomerData Empty = CustomerData.empty();

    public CustomerProjectionActor() {
        this(QueryModelStateStoreProvider.instance().store);
    }

    public CustomerProjectionActor(final StateStore stateStore) {
        super(stateStore);
    }

    @Override
    protected CustomerData currentDataFor(final Projectable projectable) {
        return Empty;
    }

    @Override
    protected CustomerData merge(final CustomerData previousData, final int previousVersion, final CustomerData currentData, final int currentVersion) {

        if (previousVersion == currentVersion) return currentData;

        CustomerData merged = previousData;

        for (final Source<?> event : sources()) {
            switch (Events.valueOf(event.typeName())) {
                case CustomerCreated: {
                    final CustomerCreated typedEvent = typed(event);
                    final AddressData address = AddressData.from(typedEvent.address.firstLine, typedEvent.address.secondLine);
                    final LegalStatusData legalStatus = LegalStatusData.from(typedEvent.legalStatus.fiscalCode, typedEvent.legalStatus.patent, typedEvent.legalStatus.commercialRegistry);
                    final var associates = typedEvent.associates == null ? new HashSet<AssociateData>() : typedEvent.associates.stream().map(item -> AssociateData.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet());
                    final CapitalData capital = CapitalData.from(typedEvent.capital.value);
                    merged = CustomerData.from(typedEvent.id, typedEvent.name, typedEvent.type, typedEvent.activityType, typedEvent.creationDate, capital, address, legalStatus, associates);
                    break;
                }

                case AssociatesAdded: {
                    final AssociatesAdded typedEvent = typed(event);
                    final var associates = typedEvent.associates == null ? new HashSet<AssociateData>() : typedEvent.associates.stream().map(item -> AssociateData.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet());
                    merged = CustomerData.from(typedEvent.id, previousData.name, previousData.type, previousData.activityType, previousData.creationDate, previousData.capital, previousData.address, previousData.legalStatus, associates);
                    break;
                }

                case AssociatesRemoved: {
                    final AssociatesRemoved typedEvent = typed(event);
                    final var associates = typedEvent.associates == null ? new HashSet<AssociateData>() : typedEvent.associates.stream().map(item -> AssociateData.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet());
                    merged = CustomerData.from(typedEvent.id, previousData.name, previousData.type, previousData.activityType, previousData.creationDate, previousData.capital, previousData.address, previousData.legalStatus, associates);
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
