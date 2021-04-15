package com.hamzajg.accounting.employee.infrastructure.exchange;

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

        final ConnectionSettings employeesExchangeSettings =
                ExchangeSettings.of("employees-exchange").mapToConnection();

        final Exchange employeesExchange =
                ExchangeFactory.fanOutInstance(employeesExchangeSettings, "employees-exchange", true);

        employeesExchange.register(Covey.of(
                new MessageSender(employeesExchange.connection()),
                received -> {
                },
                new EmployeeProducerAdapter(),
                IdentifiedDomainEvent.class,
                IdentifiedDomainEvent.class,
                Message.class));


        this.dispatcher = new ExchangeDispatcher(employeesExchange);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            employeesExchange.close();

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