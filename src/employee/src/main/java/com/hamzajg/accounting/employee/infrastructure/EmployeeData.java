package com.hamzajg.accounting.employee.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import com.hamzajg.accounting.employee.model.employee.EmployeeState;

public class EmployeeData {
  public final String id;
  public final FullNameData fullName;
  public final AddressData address;
  public final double workingPeriod;
  public final MoneyData cost;

  public static EmployeeData from(final EmployeeState employeeState) {
    final FullNameData fullName = employeeState.fullName != null ? FullNameData.from(employeeState.fullName) : null;
    final AddressData address = employeeState.address != null ? AddressData.from(employeeState.address) : null;
    final MoneyData cost = employeeState.cost != null ? MoneyData.from(employeeState.cost) : null;
    return from(employeeState.id, fullName, address, employeeState.workingPeriod, cost);
  }

  public static EmployeeData from(final String id, final FullNameData fullName, final AddressData address, final double workingPeriod, final MoneyData cost) {
    return new EmployeeData(id, fullName, address, workingPeriod, cost);
  }

  public static List<EmployeeData> from(final List<EmployeeState> states) {
    return states.stream().map(EmployeeData::from).collect(Collectors.toList());
  }

  public static EmployeeData empty() {
    return from(EmployeeState.identifiedBy(""));
  }

  private EmployeeData (final String id, final FullNameData fullName, final AddressData address, final double workingPeriod, final MoneyData cost) {
    this.id = id;
    this.fullName = fullName;
    this.address = address;
    this.workingPeriod = workingPeriod;
    this.cost = cost;
  }

}
