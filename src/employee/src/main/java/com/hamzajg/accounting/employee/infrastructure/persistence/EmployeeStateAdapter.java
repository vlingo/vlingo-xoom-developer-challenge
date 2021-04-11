package com.hamzajg.accounting.employee.infrastructure.persistence;

import com.hamzajg.accounting.employee.model.employee.EmployeeState;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateadapter-and-stateadapterprovider">
 *   StateAdapter and StateAdapterProvider
 * </a>
 */
public final class EmployeeStateAdapter implements StateAdapter<EmployeeState,TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public EmployeeState fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public <ST> ST fromRawState(final TextState raw, final Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }

  @Override
  public TextState toRawState(final String id, final EmployeeState state, final int stateVersion, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(id, EmployeeState.class, typeVersion(), serialization, stateVersion, metadata);
  }
}
