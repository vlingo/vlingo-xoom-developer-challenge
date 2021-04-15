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
        banksExchange.close();
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