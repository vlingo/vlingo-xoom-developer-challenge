package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class ExerciseQueriesActor extends StateStoreQueryActor implements ExerciseQueries {

    public ExerciseQueriesActor(StateStore store) {
        super(store);
    }

    @Override
    public Completes<ExerciseData> exerciseOf(String id) {
        return queryStateFor(id, ExerciseData.class, ExerciseData.empty());
    }

    @Override
    public Completes<Collection<ExerciseData>> exercises() {
        return streamAllOf(ExerciseData.class, new ArrayList<>());
    }

}
