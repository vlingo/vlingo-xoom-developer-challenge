package com.hamzajg.accounting.purchase.infrastructure.exchange;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;
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

        final ConnectionSettings purchasesSettings =
                ExchangeSettings.of("purchases").mapToConnection();

        final Exchange purchases =
                ExchangeFactory.fanOutInstance(purchasesSettings, "purchases", true);

        purchases.register(Covey.of(
                new MessageSender(purchases.connection()),
                new VendorExchangeReceivers.VendorCreated(stage),
                new VendorConsumerAdapter("Demo:Accounting:Purchase:VendorCreated:0.0.1"),
                VendorData.class,
                String.class,
                Message.class));

        final ConnectionSettings purchaseExchangeSettings =
                ExchangeSettings.of("purchases-exchange").mapToConnection();

        final Exchange purchasesExchange =
                ExchangeFactory.fanOutInstance(purchaseExchangeSettings, "purchases-exchange", true);

        purchasesExchange.register(Covey.of(
                new MessageSender(purchasesExchange.connection()),
                received -> {
                },
                new VendorProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));


        this.dispatcher = new ExchangeDispatcher(purchasesExchange);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            purchases.close();
            purchasesExchange.close();

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