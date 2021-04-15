package com.hamzajg.accounting.selling.model.client;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Client {

    Completes<ClientState> create(final String name, final String activityType);

    static Completes<ClientState> create(final Stage stage, final String name, final String activityType) {
        final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
        final Client _client = stage.actorFor(Client.class, Definition.has(ClientEntity.class, Definition.parameters(_address.idString())), _address);
        return _client.create(name, activityType);
    }

}