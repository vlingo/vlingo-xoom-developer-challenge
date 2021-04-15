package com.hamzajg.accounting.selling.infrastructure.exchange;

import com.hamzajg.accounting.selling.infrastructure.ClientData;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.Covey;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;

public class ExchangeBootstrap implements ExchangeInitializer {

    private Dispatcher dispatcher;

    @Override
    public void init(final Grid stage) {
        ExchangeSettings.load(Settings.properties());

        final ConnectionSettings sellingSettings =
                ExchangeSettings.of("selling").mapToConnection();

        final Exchange selling =
                ExchangeFactory.fanOutInstance(sellingSettings, "selling", true);

        selling.register(Covey.of(
                new MessageSender(selling.connection()),
                new ClientExchangeReceivers.ClientCreated(stage),
                new ClientConsumerAdapter("Demo:Accounting:Selling:ClientCreated:0.0.1"),
                ClientData.class,
                String.class,
                Message.class));

        final ConnectionSettings sellingExchangeSettings =
                ExchangeSettings.of("selling-exchange").mapToConnection();

        final Exchange sellingExchange =
                ExchangeFactory.fanOutInstance(sellingExchangeSettings, "selling-exchange", true);

        sellingExchange.register(Covey.of(
                new MessageSender(sellingExchange.connection()),
                received -> {
                },
                new ClientProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));


        this.dispatcher = new ExchangeDispatcher(sellingExchange);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            selling.close();
            sellingExchange.close();

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