package com.hamzajg.accounting.buying.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.buying.infrastructure.VendorData;

public interface VendorQueries {
  Completes<VendorData> vendorOf(String id);
  Completes<Collection<VendorData>> vendors();
}