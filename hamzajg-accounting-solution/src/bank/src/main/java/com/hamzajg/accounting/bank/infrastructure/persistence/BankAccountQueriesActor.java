package com.hamzajg.accounting.bank.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class BankAccountQueriesActor extends StateStoreQueryActor implements BankAccountQueries {

  public BankAccountQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<BankAccountData> bankAccountOf(String id) {
    return queryStateFor(id, BankAccountData.class, BankAccountData.empty());
  }

  @Override
  public Completes<Collection<BankAccountData>> bankAccounts() {
    return streamAllOf(BankAccountData.class, new ArrayList<>());
  }

}
