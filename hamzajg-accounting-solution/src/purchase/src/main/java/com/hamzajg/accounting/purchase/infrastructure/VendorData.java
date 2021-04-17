package com.hamzajg.accounting.purchase.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import com.hamzajg.accounting.purchase.model.vendor.VendorState;

public class VendorData {
  public final String id;
  public final String name;
  public final String activityType;

  public static VendorData from(final VendorState vendorState) {
    return from(vendorState.id, vendorState.name, vendorState.activityType);
  }

  public static VendorData from(final String id, final String name, final String activityType) {
    return new VendorData(id, name, activityType);
  }

  public static List<VendorData> from(final List<VendorState> states) {
    return states.stream().map(VendorData::from).collect(Collectors.toList());
  }

  public static VendorData empty() {
    return from(VendorState.identifiedBy(""));
  }

  private VendorData (final String id, final String name, final String activityType) {
    this.id = id;
    this.name = name;
    this.activityType = activityType;
  }

}
