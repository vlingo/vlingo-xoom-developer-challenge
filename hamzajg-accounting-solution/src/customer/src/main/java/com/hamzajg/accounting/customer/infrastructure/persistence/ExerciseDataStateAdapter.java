package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

public class ExerciseDataStateAdapter implements StateAdapter<ExerciseData,TextState> {

    @Override
    public int typeVersion() {
      return 1;
    }
  
    @Override
    public ExerciseData fromRawState(final TextState raw) {
      return JsonSerialization.deserialized(raw.data, raw.typed());
    }
  
    @Override
    public TextState toRawState(String id, ExerciseData state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(id, ExerciseData.class, typeVersion(), serialization, stateVersion, metadata);
    }
    
    @Override
    public TextState toRawState(ExerciseData state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(state.id, ExerciseData.class, typeVersion(), serialization, stateVersion, metadata);
    }
  
    @Override
    public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
      return JsonSerialization.deserialized(raw.data, stateType);
    }

}
