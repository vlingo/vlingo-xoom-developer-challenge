package com.hamzajg.accounting.customer.infrastructure.exchange;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.dispatch.ConfirmDispatchedResultInterest;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.stream.Collectors;

import com.hamzajg.accounting.customer.model.exercise.ExerciseEndDateChanged;
import com.hamzajg.accounting.customer.model.exercise.ExerciseCreated;
import com.hamzajg.accounting.customer.model.exercise.ExerciseClosed;
import com.hamzajg.accounting.customer.model.customer.CustomerCreated;
import com.hamzajg.accounting.customer.model.customer.AssociatesRemoved;
import com.hamzajg.accounting.customer.model.exercise.ExerciseStartDateChanged;
import com.hamzajg.accounting.customer.model.customer.AssociatesAdded;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#dispatcher-and-projectiondispatcher">
 *   Dispatcher and ProjectionDispatcher
 * </a>
 */
public class ExchangeDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<String>>>, ConfirmDispatchedResultInterest {
  private static final Logger logger = LoggerFactory.getLogger(ExchangeDispatcher.class);

  private DispatcherControl control;
  private final List<Exchange> producerExchanges;
  private final Map<String, Set<String>> eventsByExchangeName = new HashMap<>();

  public ExchangeDispatcher(final Exchange ...producerExchanges) {
    this.eventsByExchangeName.put("customers", new HashSet<>());
    this.eventsByExchangeName.get("customers").add(AssociatesRemoved.class.getCanonicalName());
    this.eventsByExchangeName.get("customers").add(CustomerCreated.class.getCanonicalName());
    this.eventsByExchangeName.get("customers").add(AssociatesAdded.class.getCanonicalName());
    this.eventsByExchangeName.put("exercises", new HashSet<>());
    this.eventsByExchangeName.get("exercises").add(ExerciseCreated.class.getCanonicalName());
    this.eventsByExchangeName.get("exercises").add(ExerciseStartDateChanged.class.getCanonicalName());
    this.eventsByExchangeName.get("exercises").add(ExerciseClosed.class.getCanonicalName());
    this.eventsByExchangeName.get("exercises").add(ExerciseEndDateChanged.class.getCanonicalName());
    this.producerExchanges = Arrays.asList(producerExchanges);
  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<String>> dispatchable) {
    logger.debug("Going to dispatch id {}", dispatchable.id());

    for (Entry<String> entry : dispatchable.entries()) {
      this.send(JsonSerialization.deserialized(entry.entryData(), entry.typed()));
    }

    this.control.confirmDispatched(dispatchable.id(), this);
  }

  @Override
  public void confirmDispatchedResultedIn(Result result, String dispatchId) {
      logger.debug("Dispatch id {} resulted in {}", dispatchId, result);
  }

  @Override
  public void controlWith(DispatcherControl control) {
    this.control = control;
  }

  private void send(final Object event) {
    this.findInterestedIn(event).forEach(exchange -> exchange.send(event));
  }

  private Stream<Exchange> findInterestedIn(final Object event) {
    final Set<String> exchangeNames =
          eventsByExchangeName.entrySet().stream().filter(exchange -> {
             final Set<String> events = exchange.getValue();
             return events.contains(event.getClass().getCanonicalName());
         }).map(Map.Entry::getKey).collect(Collectors.toSet());

    return this.producerExchanges.stream().filter(exchange -> exchangeNames.contains(exchange.name()));
  }

}
