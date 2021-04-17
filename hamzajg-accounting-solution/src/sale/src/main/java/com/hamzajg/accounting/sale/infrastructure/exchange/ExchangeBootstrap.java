package com.hamzajg.accounting.sale.infrastructure.exchange;

import com.hamzajg.accounting.sale.infrastructure.ClientData;
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

        final ConnectionSettings salesExchangeSettings =
                ExchangeSettings.of("sales-exchange").mapToConnection();

        final Exchange salesExchange =
                ExchangeFactory.fanOutInstance(salesExchangeSettings, "sales-exchange", true);

        salesExchange.register(Covey.of(
                new MessageSender(salesExchange.connection()),
                received -> {
                },
                new ClientProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));


        this.dispatcher = new ExchangeDispatcher(salesExchange);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            salesExchange.close();

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