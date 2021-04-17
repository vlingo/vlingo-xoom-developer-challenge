package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.bankaccount.BankAccountCreated;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccountEntity;
import com.hamzajg.accounting.bank.model.journal.*;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;

import java.util.Arrays;

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
    public static CommandModelJournalStoreProvider using(final Stage stage, final SourcedTypeRegistry registry,
            final Dispatcher... dispatchers) {
        if (instance != null) {
            return instance;
        }

        final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

        entryAdapterProvider.registerAdapter(JournalLinesRemoved.class, new JournalLinesRemovedAdapter());
        entryAdapterProvider.registerAdapter(JournalDescriptionChanged.class, new JournalDescriptionChangedAdapter());
        entryAdapterProvider.registerAdapter(JournalCreated.class, new JournalCreatedAdapter());
        entryAdapterProvider.registerAdapter(JournalLinesChanged.class, new JournalLinesChangedAdapter());
        entryAdapterProvider.registerAdapter(JournalDateChanged.class, new JournalDateChangedAdapter());
        entryAdapterProvider.registerAdapter(JournalLinesAdded.class, new JournalLinesAddedAdapter());
        entryAdapterProvider.registerAdapter(BankAccountCreated.class, new BankAccountCreatedAdapter());

        final Journal<String> journal = StoreActorBuilder.from(stage, Model.COMMAND, Arrays.asList(dispatchers),
                StorageType.JOURNAL, Settings.properties(), true);

        registry.register(new Info(journal, JournalEntity.class, JournalEntity.class.getSimpleName()));
        registry.register(new Info(journal, BankAccountEntity.class, BankAccountEntity.class.getSimpleName()));

        instance = new CommandModelJournalStoreProvider(journal);

        return instance;
    }

    private CommandModelJournalStoreProvider(final Journal<String> journal) {
        this.journal = journal;
    }

    public static void reset() {
        instance = null;
    }

}
