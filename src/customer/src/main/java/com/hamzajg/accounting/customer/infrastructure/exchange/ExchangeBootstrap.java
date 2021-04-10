package com.hamzajg.accounting.customer.infrastructure.exchange;

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
import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import com.hamzajg.accounting.customer.infrastructure.CustomerData;

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
        new CustomerExchangeReceivers.CustomerCreated(stage),
        new CustomerConsumerAdapter("Demo:Accounting:Customer:CustomerCreated:0.0.1"),
        CustomerData.class,
        String.class,
        Message.class));

    customersExchange.register(Covey.of(
        new MessageSender(customersExchange.connection()),
        new CustomerExchangeReceivers.AssociatesAdded(stage),
        new CustomerConsumerAdapter("Demo:Accounting:Customer:AssociatesAdded:0.0.1"),
        CustomerData.class,
        String.class,
        Message.class));

    customersExchange.register(Covey.of(
        new MessageSender(customersExchange.connection()),
        new CustomerExchangeReceivers.AssociatesRemoved(stage),
        new CustomerConsumerAdapter("Demo:Accounting:Customer:AssociatesRemoved:0.0.1"),
        CustomerData.class,
        String.class,
        Message.class));

    final ConnectionSettings customersSettings =
                ExchangeSettings.of("customers").mapToConnection();

    final Exchange customers =
                ExchangeFactory.fanOutInstance(customersSettings, "customers", true);

    customers.register(Covey.of(
        new MessageSender(customers.connection()),
        received -> {},
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
        new ExerciseExchangeReceivers.ExerciseCreated(stage),
        new ExerciseConsumerAdapter("Demo:Accounting:Customer:ExerciseCreated:0.0.1"),
        ExerciseData.class,
        String.class,
        Message.class));

    exercisesExchange.register(Covey.of(
        new MessageSender(exercisesExchange.connection()),
        new ExerciseExchangeReceivers.ExerciseEndDateChanged(stage),
        new ExerciseConsumerAdapter("Demo:Accounting:Customer:ExerciseEndDateChanged:0.0.1"),
        ExerciseData.class,
        String.class,
        Message.class));

    exercisesExchange.register(Covey.of(
        new MessageSender(exercisesExchange.connection()),
        new ExerciseExchangeReceivers.ExerciseClosed(stage),
        new ExerciseConsumerAdapter("Demo:Accounting:Customer:ExerciseClosed:0.0.1"),
        ExerciseData.class,
        String.class,
        Message.class));

    exercisesExchange.register(Covey.of(
        new MessageSender(exercisesExchange.connection()),
        new ExerciseExchangeReceivers.ExerciseStartDateChanged(stage),
        new ExerciseConsumerAdapter("Demo:Accounting:Customer:ExerciseStartDateChanged:0.0.1"),
        ExerciseData.class,
        String.class,
        Message.class));

    final ConnectionSettings exercisesSettings =
                ExchangeSettings.of("exercises").mapToConnection();

    final Exchange exercises =
                ExchangeFactory.fanOutInstance(exercisesSettings, "exercises", true);

    exercises.register(Covey.of(
        new MessageSender(exercises.connection()),
        received -> {},
        new ExerciseProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(customers, exercises);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        customersExchange.close();
        customers.close();
        exercisesExchange.close();
        exercises.close();

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