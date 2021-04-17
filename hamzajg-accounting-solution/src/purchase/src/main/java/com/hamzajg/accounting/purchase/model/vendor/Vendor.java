package com.hamzajg.accounting.purchase.model.vendor;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Vendor {

    Completes<VendorState> create(final String name, final String activityType);

    static Completes<VendorState> create(final Stage stage, final String name, final String activityType) {
        final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
        final Vendor _vendor = stage.actorFor(Vendor.class, Definition.has(VendorEntity.class, Definition.parameters(_address.idString())), _address);
        return _vendor.create(name, activityType);
    }

}