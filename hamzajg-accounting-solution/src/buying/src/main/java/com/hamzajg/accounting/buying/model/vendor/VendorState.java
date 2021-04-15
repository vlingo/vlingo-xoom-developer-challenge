package com.hamzajg.accounting.buying.model.vendor;


public final class VendorState {

  public final String id;
  public final String name;
  public final String activityType;

  public static VendorState identifiedBy(final String id) {
    return new VendorState(id, null, null);
  }

  public VendorState (final String id, final String name, final String activityType) {
    this.id = id;
    this.name = name;
    this.activityType = activityType;
  }

  public VendorState create(final String name, final String activityType) {
    return new VendorState(this.id, name, activityType);
  }

}
