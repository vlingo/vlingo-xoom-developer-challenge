package com.hamzajg.accounting.sale.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import com.hamzajg.accounting.sale.model.client.ClientState;

public class ClientData {
  public final String id;
  public final String name;
  public final String activityType;

  public static ClientData from(final ClientState clientState) {
    return from(clientState.id, clientState.name, clientState.activityType);
  }

  public static ClientData from(final String id, final String name, final String activityType) {
    return new ClientData(id, name, activityType);
  }

  public static List<ClientData> from(final List<ClientState> states) {
    return states.stream().map(ClientData::from).collect(Collectors.toList());
  }

  public static ClientData empty() {
    return from(ClientState.identifiedBy(""));
  }

  private ClientData (final String id, final String name, final String activityType) {
    this.id = id;
    this.name = name;
    this.activityType = activityType;
  }

}
