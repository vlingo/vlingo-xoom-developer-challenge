package com.hamzajg.accounting.rental.model.rentalcontract;

import io.vlingo.xoom.common.Completes;
import com.hamzajg.accounting.rental.model.*;

import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class RentalContractEntity extends StatefulEntity<RentalContractState> implements RentalContract {
  private RentalContractState state;

  public RentalContractEntity(final String id) {
    super(id);
    this.state = RentalContractState.identifiedBy(id);
  }

  @Override
  public Completes<RentalContractState> create(final String startDate, final String endDate, final String customerId, final int paymentPeriod, final Money price) {
    final RentalContractState stateArg = state.create(startDate, endDate, customerId, paymentPeriod, price);
    return apply(stateArg, new RentalContractCreated(stateArg), () -> state);
  }

  @Override
  public Completes<RentalContractState> terminate(final String terminationDate) {
    final RentalContractState stateArg = state.terminate(terminationDate);
    return apply(stateArg, new RentalContractTerminated(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the RentalContractState
   */
  @Override
  protected void state(final RentalContractState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<RentalContractState>}
   */
  @Override
  protected Class<RentalContractState> stateType() {
    return RentalContractState.class;
  }
}
