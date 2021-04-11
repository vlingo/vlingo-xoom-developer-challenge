package com.hamzajg.accounting.assets.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import com.hamzajg.accounting.assets.model.journal.JournalState;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;


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
  public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(JournalState.class, new JournalStateAdapter());

    new EntryAdapterProvider(stage.world()); // future use

    StateTypeStateStoreMap.stateTypeToStoreName(JournalState.class, JournalState.class.getSimpleName());

    final StateStore store =
            StoreActorBuilder.from(stage, Model.COMMAND, Arrays.asList(dispatchers), StorageType.STATE_STORE, Settings.properties(), true);

    registry.register(new Info(store, JournalState.class, JournalState.class.getSimpleName()));

    instance = new CommandModelStateStoreProvider(stage, store);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStateStoreProvider(final Stage stage, final StateStore store) {
    this.store = store;
  }
}
