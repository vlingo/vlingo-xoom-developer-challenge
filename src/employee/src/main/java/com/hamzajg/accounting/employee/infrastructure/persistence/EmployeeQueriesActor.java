package com.hamzajg.accounting.employee.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import com.hamzajg.accounting.employee.infrastructure.EmployeeData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class EmployeeQueriesActor extends StateStoreQueryActor implements EmployeeQueries {

  public EmployeeQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<EmployeeData> employeeOf(String id) {
    return queryStateFor(id, EmployeeData.class, EmployeeData.empty());
  }

  @Override
  public Completes<Collection<EmployeeData>> employees() {
    return streamAllOf(EmployeeData.class, new ArrayList<>());
  }

}
