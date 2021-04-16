package com.hamzajg.accounting.employee.model.employee;

import com.hamzajg.accounting.employee.model.*;

import io.vlingo.xoom.symbio.store.object.StateObject;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object
 * Storage</a>
 */
public final class EmployeeState extends StateObject {

  public final String id;
  public final String exerciseId;
  public final FullName fullName;
  public final Address address;
  public final double workingPeriod;
  public final Money cost;

  public static EmployeeState identifiedBy(final String id) {
    return new EmployeeState(id, null, null, null, 0, null);
  }

  public EmployeeState(final String id, final String exerciseId, final FullName fullName, final Address address,
      final double workingPeriod, final Money cost) {
    this.id = id;
    this.exerciseId = exerciseId;
    this.fullName = fullName;
    this.address = address;
    this.workingPeriod = workingPeriod;
    this.cost = cost;
  }

  public EmployeeState create(final String exerciseId, final FullName fullName, final Address address,
      final double workingPeriod, final Money cost) {
    return new EmployeeState(this.id, exerciseId, fullName, address, workingPeriod, cost);
  }

  public EmployeeState changeWorkingPeriod(final double workingPeriod) {
    return new EmployeeState(this.id, this.exerciseId, this.fullName, this.address, workingPeriod, this.cost);
  }

  public EmployeeState changeCost(final Money cost) {
    return new EmployeeState(this.id, this.exerciseId, this.fullName, this.address, this.workingPeriod, cost);
  }

}
