package com.hamzajg.accounting.employee.model.employee;

import com.hamzajg.accounting.employee.model.*;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Employee {

  Completes<EmployeeState> create(final FullName fullName, final Address address, final double workingPeriod, final Money cost);

  static Completes<EmployeeState> create(final Stage stage, final FullName fullName, final Address address, final double workingPeriod, final Money cost) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Employee _employee = stage.actorFor(Employee.class, Definition.has(EmployeeEntity.class, Definition.parameters(_address.idString())), _address);
    return _employee.create(fullName, address, workingPeriod, cost);
  }

  Completes<EmployeeState> changeWorkingPeriod(final double workingPeriod);

  Completes<EmployeeState> changeCost(final Money cost);

}