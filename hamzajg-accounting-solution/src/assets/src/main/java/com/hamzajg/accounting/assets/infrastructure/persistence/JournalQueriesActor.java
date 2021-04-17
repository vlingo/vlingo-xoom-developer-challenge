package com.hamzajg.accounting.assets.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import com.hamzajg.accounting.assets.infrastructure.JournalData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class JournalQueriesActor extends StateStoreQueryActor implements JournalQueries {

  public JournalQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<JournalData> journalOf(String id) {
    return queryStateFor(id, JournalData.class, JournalData.empty());
  }

  @Override
  public Completes<Collection<JournalData>> journals() {
    return streamAllOf(JournalData.class, new ArrayList<>());
  }

}
