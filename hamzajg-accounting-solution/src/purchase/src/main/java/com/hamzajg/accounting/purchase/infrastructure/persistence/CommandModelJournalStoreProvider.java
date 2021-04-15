package com.hamzajg.accounting.purchase.infrastructure.persistence;

import java.util.Arrays;

import com.hamzajg.accounting.purchase.model.vendor.VendorCreated;
import com.hamzajg.accounting.purchase.model.vendor.VendorEntity;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;

public class CommandModelJournalStoreProvider {
  private static CommandModelJournalStoreProvider instance;

  public final Journal<String> journal;

  public static CommandModelJournalStoreProvider instance() {
    return instance;
  }

  public static CommandModelJournalStoreProvider using(final Stage stage, final SourcedTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
 }

  @SuppressWarnings({ "unchecked", "unused" })
  public static CommandModelJournalStoreProvider using(final Stage stage, final SourcedTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

    entryAdapterProvider.registerAdapter(VendorCreated.class, new VendorCreatedAdapter());

    final Journal<String> journal =
              StoreActorBuilder.from(stage, Model.COMMAND, Arrays.asList(dispatchers), StorageType.JOURNAL, Settings.properties(), true);

    registry.register(new Info(journal, VendorEntity.class, VendorEntity.class.getSimpleName()));

    instance = new CommandModelJournalStoreProvider(journal);

    return instance;
  }

  private CommandModelJournalStoreProvider(final Journal<String> journal) {
    this.journal = journal;
  }

}
