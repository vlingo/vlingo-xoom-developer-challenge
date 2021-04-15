package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.*;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;

import java.util.Arrays;

public class QueryModelStateStoreProvider {
    private static QueryModelStateStoreProvider instance;

    public final StateStore store;
    public final ExerciseQueries exerciseQueries;
    public final CustomerQueries customerQueries;

    public static QueryModelStateStoreProvider instance() {
        return instance;
    }

    public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
        return using(stage, registry, new NoOpDispatcher());
    }

    @SuppressWarnings("rawtypes")
    public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher... dispatchers) {
        if (instance != null) {
            return instance;
        }

        new EntryAdapterProvider(stage.world()); // future use

        StateTypeStateStoreMap.stateTypeToStoreName(AddressData.class, AddressData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(CustomerData.class, CustomerData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(CapitalData.class, CapitalData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(ExerciseData.class, ExerciseData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(LegalStatusData.class, LegalStatusData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(AssociateData.class, AssociateData.class.getSimpleName());

        final StateStore store =
                StoreActorBuilder.from(stage, Model.QUERY, Arrays.asList(dispatchers), Persistence.StorageType.STATE_STORE, Settings.properties(), true);


        instance = new QueryModelStateStoreProvider(stage, store);

        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
        this.store = store;
        this.exerciseQueries = stage.actorFor(ExerciseQueries.class, ExerciseQueriesActor.class, store);
        this.customerQueries = stage.actorFor(CustomerQueries.class, CustomerQueriesActor.class, store);
    }

    public static void reset() {
        instance = null;
    }
}
