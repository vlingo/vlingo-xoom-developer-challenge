package com.hamzajg.accounting.purchase.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;

public interface VendorQueries {
  Completes<VendorData> vendorOf(String id);
  Completes<Collection<VendorData>> vendors();
}