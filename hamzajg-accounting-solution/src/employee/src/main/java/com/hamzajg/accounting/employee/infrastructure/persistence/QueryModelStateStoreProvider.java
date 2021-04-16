package com.hamzajg.accounting.employee.infrastructure.persistence;

import com.hamzajg.accounting.employee.infrastructure.AddressData;
import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.infrastructure.FullNameData;
import com.hamzajg.accounting.employee.infrastructure.MoneyData;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;

import java.util.Arrays;


public class QueryModelStateStoreProvider {
    private static QueryModelStateStoreProvider instance;

    public final StateStore store;
    public final EmployeeQueries employeeQueries;

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

        StateTypeStateStoreMap.stateTypeToStoreName(MoneyData.class, MoneyData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(AddressData.class, AddressData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(EmployeeData.class, EmployeeData.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(FullNameData.class, FullNameData.class.getSimpleName());

        final StateStore store =
                StoreActorBuilder.from(stage, Model.QUERY, Arrays.asList(dispatchers), StorageType.STATE_STORE, Settings.properties(), true);


        instance = new QueryModelStateStoreProvider(stage, store);

        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
        this.store = store;
        this.employeeQueries = stage.actorFor(EmployeeQueries.class, EmployeeQueriesActor.class, store);
    }

    public static void reset() {
    }
}
