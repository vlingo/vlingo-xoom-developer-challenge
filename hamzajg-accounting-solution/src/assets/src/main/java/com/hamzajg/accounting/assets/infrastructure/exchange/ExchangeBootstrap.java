package com.hamzajg.accounting.assets.infrastructure.exchange;

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

import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import com.hamzajg.accounting.assets.infrastructure.JournalData;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  @Override
  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings assetsSettings =
                ExchangeSettings.of("assets").mapToConnection();

    final Exchange assets =
                ExchangeFactory.fanOutInstance(assetsSettings, "assets", true);

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalCreated(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalCreated:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalLinesRemoved(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalLinesRemoved:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalTypeChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalTypeChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalLinesAdded(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalLinesAdded:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalTitleChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalTitleChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalDateChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalDateChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    assets.register(Covey.of(
        new MessageSender(assets.connection()),
        new JournalExchangeReceivers.JournalLinesChanged(stage),
        new JournalConsumerAdapter("Demo:Accounting:Assets:JournalLinesChanged:0.0.1"),
        JournalData.class,
        String.class,
        Message.class));

    final ConnectionSettings assetsExchangeSettings =
                ExchangeSettings.of("assets-exchange").mapToConnection();

    final Exchange assetsExchange =
                ExchangeFactory.fanOutInstance(assetsExchangeSettings, "assets-exchange", true);

    assetsExchange.register(Covey.of(
        new MessageSender(assetsExchange.connection()),
        received -> {},
        new JournalProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(assetsExchange);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        assets.close();
        assetsExchange.close();

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