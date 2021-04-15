package com.hamzajg.accounting.employee.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.model.*;
import com.hamzajg.accounting.employee.model.employee.EmployeeEntity;
import com.hamzajg.accounting.employee.model.employee.Employee;

public class EmployeeExchangeReceivers {

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class EmployeeCreated implements ExchangeReceiver<EmployeeData> {

    private final Grid stage;

    public EmployeeCreated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final EmployeeData data) {
      final FullName fullName = FullName.from(data.fullName.firstName, data.fullName.secondName, data.fullName.lastName);
      final Address address = Address.from(data.address.firstLine, data.address.secondLine);
      final Money cost = Money.from(data.cost.amount, data.cost.currency);
      Employee.create(stage, fullName, address, data.workingPeriod, cost);
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class EmployeeWorkingPeriodChanged implements ExchangeReceiver<EmployeeData> {

    private final Grid stage;

    public EmployeeWorkingPeriodChanged(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final EmployeeData data) {
      stage.actorOf(Employee.class, stage.addressFactory().from(data.id), Definition.has(EmployeeEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(employee -> employee.changeWorkingPeriod(data.workingPeriod));
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class EmployeeCostChanged implements ExchangeReceiver<EmployeeData> {

    private final Grid stage;

    public EmployeeCostChanged(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final EmployeeData data) {
      final Money cost = Money.from(data.cost.amount, data.cost.currency);
      stage.actorOf(Employee.class, stage.addressFactory().from(data.id), Definition.has(EmployeeEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(employee -> employee.changeCost(cost));
    }
  }

}
