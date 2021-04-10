package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

import java.util.Arrays;

public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final CustomerQueries customerQueries;
  public final ExerciseQueries exerciseQueries;
  public final StateStore store;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    if (instance != null)
      return instance;

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(CustomerData.class, new CustomerDataStateAdapter());
    stateAdapterProvider.registerAdapter(ExerciseData.class, new ExerciseDataStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    final StateStore store = stage.actorFor(StateStore.class, InMemoryStateStoreActor.class,
        Arrays.asList(new NoOpDispatcher()));

    final CustomerQueries customerQueries = stage.actorFor(CustomerQueries.class, CustomerQueriesActor.class, store);
    final ExerciseQueries exerciseQueries = stage.actorFor(ExerciseQueries.class, ExerciseQueriesActor.class, store);

    instance = new QueryModelStateStoreProvider(registry, store, customerQueries, exerciseQueries);

    return instance;
  }

  public static void reset() {
    instance = null;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final StatefulTypeRegistry registry, final StateStore store,
                                       final CustomerQueries customerQueries, final ExerciseQueries exerciseQueries) {
    this.store = store;
    this.customerQueries = customerQueries;
    this.exerciseQueries = exerciseQueries;

    registry.register(new Info(store, CustomerData.class, CustomerData.class.getSimpleName()));
    registry.register(new Info(store, ExerciseData.class, ExerciseData.class.getSimpleName()));
  }
}
