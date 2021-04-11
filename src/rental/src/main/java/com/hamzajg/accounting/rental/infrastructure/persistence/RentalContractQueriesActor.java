package com.hamzajg.accounting.rental.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import com.hamzajg.accounting.rental.infrastructure.RentalContractData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class RentalContractQueriesActor extends StateStoreQueryActor implements RentalContractQueries {

  public RentalContractQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<RentalContractData> rentalContractOf(String id) {
    return queryStateFor(id, RentalContractData.class, RentalContractData.empty());
  }

  @Override
  public Completes<Collection<RentalContractData>> rentalContracts() {
    return streamAllOf(RentalContractData.class, new ArrayList<>());
  }

}
