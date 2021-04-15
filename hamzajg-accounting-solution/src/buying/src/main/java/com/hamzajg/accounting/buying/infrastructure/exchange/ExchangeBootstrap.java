package com.hamzajg.accounting.buying.infrastructure.exchange;

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
import com.hamzajg.accounting.buying.infrastructure.VendorData;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  @Override
  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings buyingSettings =
                ExchangeSettings.of("buying").mapToConnection();

    final Exchange buying =
                ExchangeFactory.fanOutInstance(buyingSettings, "buying", true);

    buying.register(Covey.of(
        new MessageSender(buying.connection()),
        new VendorExchangeReceivers.VendorCreated(stage),
        new VendorConsumerAdapter("Demo:Accouting:Buying:VendorCreated:0.0.1"),
        VendorData.class,
        String.class,
        Message.class));

    final ConnectionSettings buyingExchangeSettings =
                ExchangeSettings.of("buying-exchange").mapToConnection();

    final Exchange buyingExchange =
                ExchangeFactory.fanOutInstance(buyingExchangeSettings, "buying-exchange", true);

    buyingExchange.register(Covey.of(
        new MessageSender(buyingExchange.connection()),
        received -> {},
        new VendorProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(buyingExchange);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        buying.close();
        buyingExchange.close();

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