package com.hamzajg.accounting.buying.infrastructure.persistence;

import com.hamzajg.accounting.buying.infrastructure.VendorData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class VendorQueriesActor extends StateStoreQueryActor implements VendorQueries {

    public VendorQueriesActor(StateStore store) {
        super(store);
    }

    @Override
    public Completes<VendorData> vendorOf(String id) {
        return queryStateFor(id, VendorData.class, VendorData.empty());
    }

    @Override
    public Completes<Collection<VendorData>> vendors() {
        return streamAllOf(VendorData.class, new ArrayList<>());
    }

}
