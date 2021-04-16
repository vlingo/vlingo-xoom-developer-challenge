package com.hamzajg.accounting.bank.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.infrastructure.persistence.BankAccountQueries;
import com.hamzajg.accounting.bank.infrastructure.persistence.JournalQueries;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccountEntity;
import com.hamzajg.accounting.bank.infrastructure.persistence.BankAccountQueriesActor;
import com.hamzajg.accounting.bank.infrastructure.JournalData;
import com.hamzajg.accounting.bank.infrastructure.persistence.JournalQueriesActor;
import com.hamzajg.accounting.bank.infrastructure.JournalLineData;
import com.hamzajg.accounting.bank.infrastructure.MoneyData;
import com.hamzajg.accounting.bank.model.journal.JournalEntity;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
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

public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final StateStore store;
  public final BankAccountQueries bankAccountQueries;
  public final JournalQueries journalQueries;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry,
      final Dispatcher... dispatchers) {
    if (instance != null) {
      return instance;
    }

    new EntryAdapterProvider(stage.world()); // future use

    StateTypeStateStoreMap.stateTypeToStoreName(BankAccountData.class, BankAccountData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(MoneyData.class, MoneyData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(JournalData.class, JournalData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(JournalLineData.class, JournalLineData.class.getSimpleName());

    final StateStore store = StoreActorBuilder.from(stage, Model.QUERY, Arrays.asList(dispatchers),
        StorageType.STATE_STORE, Settings.properties(), true);

    instance = new QueryModelStateStoreProvider(stage, store);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
    this.store = store;
    this.bankAccountQueries = stage.actorFor(BankAccountQueries.class, BankAccountQueriesActor.class, store);
    this.journalQueries = stage.actorFor(JournalQueries.class, JournalQueriesActor.class, store);
  }

  public static void reset() {
    instance = null;
  }
}
