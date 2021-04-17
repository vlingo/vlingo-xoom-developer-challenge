package com.hamzajg.accounting.customer.infrastructure.exchange;

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

        final ConnectionSettings customersExchangeSettings =
                ExchangeSettings.of("customers-exchange").mapToConnection();

        final Exchange customersExchange =
                ExchangeFactory.fanOutInstance(customersExchangeSettings, "customers-exchange", true);

        customersExchange.register(Covey.of(
                new MessageSender(customersExchange.connection()),
                received -> {
                },
                new CustomerProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));

        final ConnectionSettings exercisesExchangeSettings =
                ExchangeSettings.of("exercises-exchange").mapToConnection();

        final Exchange exercisesExchange =
                ExchangeFactory.fanOutInstance(exercisesExchangeSettings, "exercises-exchange", true);

        exercisesExchange.register(Covey.of(
                new MessageSender(exercisesExchange.connection()),
                received -> {
                },
                new ExerciseProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));


        this.dispatcher = new ExchangeDispatcher(customersExchange, exercisesExchange);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            customersExchange.close();
            exercisesExchange.close();

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