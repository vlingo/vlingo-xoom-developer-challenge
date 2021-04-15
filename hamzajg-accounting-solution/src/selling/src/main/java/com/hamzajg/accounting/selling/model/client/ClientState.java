package com.hamzajg.accounting.selling.model.client;


public final class ClientState {

  public final String id;
  public final String name;
  public final String activityType;

  public static ClientState identifiedBy(final String id) {
    return new ClientState(id, null, null);
  }

  public ClientState (final String id, final String name, final String activityType) {
    this.id = id;
    this.name = name;
    this.activityType = activityType;
  }

  public ClientState create(final String name, final String activityType) {
    return new ClientState(this.id, name, activityType);
  }

}
