package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.model.customer.CustomerState;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

public class CustomerStateAdapter  implements StateAdapter<CustomerState,TextState> {

    @Override
    public int typeVersion() {
      return 1;
    }
  
    @Override
    public CustomerState fromRawState(final TextState raw) {
      return JsonSerialization.deserialized(raw.data, raw.typed());
    }
  
    @Override
    public TextState toRawState(String id, CustomerState state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(id, CustomerState.class, typeVersion(), serialization, stateVersion, metadata);
    }
  
    @Override
    public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
      return JsonSerialization.deserialized(raw.data, stateType);
    }

}
