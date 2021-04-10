package com.hamzajg.accounting.customer.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class CustomerQueriesActor extends StateStoreQueryActor implements CustomerQueries {

  public CustomerQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<CustomerData> customerOf(String id) {
    return queryStateFor(id, CustomerData.class, CustomerData.empty());
  }

  @Override
  public Completes<Collection<CustomerData>> customers() {
    return streamAllOf(CustomerData.class, new ArrayList<>());
  }

}
