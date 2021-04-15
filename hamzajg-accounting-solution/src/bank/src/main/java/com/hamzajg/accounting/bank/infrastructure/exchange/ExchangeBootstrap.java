package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.xoom.lattice.exchange.Covey;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import com.hamzajg.accounting.bank.infrastructure.JournalData;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  @Override
  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings bankSettings =
                ExchangeSettings.of("bank").mapToConnection();

    final Exchange bank =
                ExchangeFactory.fanOutInstance(bankSettings, "bank", true);

    bank.register(Covey.of(
        new MessageSender(bank.connection()),
        new BankAccountExchangeReceivers.BankAccountCreated(stage),
        new BankAccountConsumerAdapter("Demo:Accounting:Bank:BankAccountCreated:0.0.1"),
        BankAccountData.class,
        String.class,
        Message.class));

    final ConnectionSettings banksExchangeSettings =
                ExchangeSettings.of("banks-exchange").mapToConnection();

    final Exchange banksExchange =
                ExchangeFactory.fanOutInstance(banksExchangeSettings, "banks-exchange", true);

    banksExchange.register(Covey.of(
        new MessageSender(banksExchange.connection()),
        received -> {},
        new BankAccountProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    final ConnectionSettings bankJournalSettings =
                ExchangeSettings.of("bank-journal").mapToConnection();

    final Exchange bankJournal =
                ExchangeFactory.fanOutInstance(bankJournalSettings, "bank-journal", true);

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalDateChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalDateChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalLinesAdded(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalLinesAdded:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalLinesChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalLinesChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalCreated(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalCreated:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalDescriptionChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalDescriptionChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    bankJournal.register(Covey.of(
        new MessageSender(bankJournal.connection()),
        new JournalExchangeReceivers.JournalLinesRemoved(stage),
        new JournalConsumerAdapter("Demo:Accounting:Bank:JournalLinesRemoved:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    final ConnectionSettings bankJournalsExchangeSettings =
                ExchangeSettings.of("bank-journals-exchange").mapToConnection();

    final Exchange bankJournalsExchange =
                ExchangeFactory.fanOutInstance(bankJournalsExchangeSettings, "bank-journals-exchange", true);

    bankJournalsExchange.register(Covey.of(
        new MessageSender(bankJournalsExchange.connection()),
        received -> {},
        new JournalProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(banksExchange, bankJournalsExchange);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        bank.close();
        banksExchange.close();
        bankJournal.close();
        bankJournalsExchange.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));
  }

  @Override
  public Dispatcher dispatcher() {
    return dispatcher;
  }
}