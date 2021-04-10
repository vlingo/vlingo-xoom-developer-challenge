package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.model.customer.CustomerState;
import com.hamzajg.accounting.customer.model.exercise.ExerciseState;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

import java.util.Arrays;

public class CommandModelStateStoreProvider {
  private static CommandModelStateStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStateStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static CommandModelStateStoreProvider using(Stage stage, StatefulTypeRegistry registry, Dispatcher dispatcher) {
    if (instance != null) return instance;
    
    StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(CustomerState.class, new CustomerStateAdapter());
    stateAdapterProvider.registerAdapter(ExerciseState.class, new ExerciseStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(InMemoryStateStoreActor.class, Definition.parameters(Arrays.asList(dispatcher))));

    Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStateStoreProvider(registry, storeWithControl._1, storeWithControl._2);

    return instance;
  }

  public static void reset() {
    instance = null;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStateStoreProvider(StatefulTypeRegistry registry, StateStore store, DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

    registry.register(new Info(store, CustomerState.class, CustomerState.class.getSimpleName()));
    registry.register(new Info(store, ExerciseState.class, ExerciseState.class.getSimpleName()));
  }
}
