package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

public class CustomerDataStateAdapter implements StateAdapter<CustomerData,TextState> {

    @Override
    public int typeVersion() {
      return 1;
    }
  
    @Override
    public CustomerData fromRawState(final TextState raw) {
      return JsonSerialization.deserialized(raw.data, raw.typed());
    }
  
    @Override
    public TextState toRawState(String id, CustomerData state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(id, CustomerData.class, typeVersion(), serialization, stateVersion, metadata);
    }
    
    @Override
    public TextState toRawState(CustomerData state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(state.id, CustomerData.class, typeVersion(), serialization, stateVersion, metadata);
    }
  
    @Override
    public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
      return JsonSerialization.deserialized(raw.data, stateType);
    }

}
