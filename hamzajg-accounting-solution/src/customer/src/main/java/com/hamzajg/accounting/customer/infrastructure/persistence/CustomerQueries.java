package com.hamzajg.accounting.customer.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;

public interface CustomerQueries {
  Completes<CustomerData> customerOf(String id);
  Completes<Collection<CustomerData>> customers();
}