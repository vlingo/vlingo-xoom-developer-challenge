package com.hamzajg.accounting.rental.infrastructure.exchange;

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
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  @Override
  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings rentalsSettings =
                ExchangeSettings.of("rentals").mapToConnection();

    final Exchange rentals =
                ExchangeFactory.fanOutInstance(rentalsSettings, "rentals", true);

    rentals.register(Covey.of(
        new MessageSender(rentals.connection()),
        new RentalContractExchangeReceivers.RentalContractTerminated(stage),
        new RentalContractConsumerAdapter("Demo:Accounting:Rental:RentalContractTerminated:0.0.1"),
        RentalContractData.class,
        String.class,
        Message.class));

    rentals.register(Covey.of(
        new MessageSender(rentals.connection()),
        new RentalContractExchangeReceivers.RentalContractCreated(stage),
        new RentalContractConsumerAdapter("Demo:Accounting:Rental:RentalContractCreated:0.0.1"),
        RentalContractData.class,
        String.class,
        Message.class));

    final ConnectionSettings rentalsExchangeSettings =
                ExchangeSettings.of("rentals-exchange").mapToConnection();

    final Exchange rentalsExchange =
                ExchangeFactory.fanOutInstance(rentalsExchangeSettings, "rentals-exchange", true);

    rentalsExchange.register(Covey.of(
        new MessageSender(rentalsExchange.connection()),
        received -> {},
        new RentalContractProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(rentalsExchange);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        rentals.close();
        rentalsExchange.close();

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