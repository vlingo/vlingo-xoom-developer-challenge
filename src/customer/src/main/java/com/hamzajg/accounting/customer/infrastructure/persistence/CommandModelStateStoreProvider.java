package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.model.customer.CustomerState;
import com.hamzajg.accounting.customer.model.exercise.ExerciseState;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;

import java.util.Arrays;

public class CommandModelStateStoreProvider {
    private static CommandModelStateStoreProvider instance;

    public final StateStore store;

    public static CommandModelStateStoreProvider instance() {
        return instance;
    }

    public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
        return using(stage, registry, new NoOpDispatcher());
    }

    @SuppressWarnings("rawtypes")
    public static CommandModelStateStoreProvider using(Stage stage, StatefulTypeRegistry registry, Dispatcher... dispatchers) {
        if (instance != null) return instance;

        final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
        stateAdapterProvider.registerAdapter(CustomerState.class, new CustomerStateAdapter());
        stateAdapterProvider.registerAdapter(ExerciseState.class, new ExerciseStateAdapter());

        new EntryAdapterProvider(stage.world()); // future use

        StateTypeStateStoreMap.stateTypeToStoreName(CustomerState.class, CustomerState.class.getSimpleName());
        StateTypeStateStoreMap.stateTypeToStoreName(ExerciseState.class, ExerciseState.class.getSimpleName());

        final StateStore store =
                StoreActorBuilder.from(stage, Model.COMMAND, Arrays.asList(dispatchers), Persistence.StorageType.STATE_STORE, Settings.properties(), true);

        registry.register(new Info(store, CustomerState.class, CustomerState.class.getSimpleName()));
        registry.register(new Info(store, ExerciseState.class, ExerciseState.class.getSimpleName()));

        instance = new CommandModelStateStoreProvider(stage, store);

        return instance;
    }

    public static void reset() {
        instance = null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private CommandModelStateStoreProvider(final Stage stage, final StateStore store) {
        this.store = store;
    }
}
