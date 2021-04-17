package com.hamzajg.accounting.employee.model.employee;

import com.hamzajg.accounting.employee.model.*;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class EmployeeEntity extends StatefulEntity<EmployeeState> implements Employee {
  private EmployeeState state;

  public EmployeeEntity(final String id) {
    super(id);
    this.state = EmployeeState.identifiedBy(id);
  }

  @Override
  public Completes<EmployeeState> create(final String exerciseId, final FullName fullName, final Address address, final double workingPeriod, final Money cost) {
    final EmployeeState stateArg = state.create(exerciseId, fullName, address, workingPeriod, cost);
    return apply(stateArg, new EmployeeCreated(stateArg), () -> state);
  }

  @Override
  public Completes<EmployeeState> changeWorkingPeriod(final double workingPeriod) {
    final EmployeeState stateArg = state.changeWorkingPeriod(workingPeriod);
    return apply(stateArg, new EmployeeWorkingPeriodChanged(stateArg), () -> state);
  }

  @Override
  public Completes<EmployeeState> changeCost(final Money cost) {
    final EmployeeState stateArg = state.changeCost(cost);
    return apply(stateArg, new EmployeeCostChanged(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the EmployeeState
   */
  @Override
  protected void state(final EmployeeState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<EmployeeState>}
   */
  @Override
  protected Class<EmployeeState> stateType() {
    return EmployeeState.class;
  }
}
