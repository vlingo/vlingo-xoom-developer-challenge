package com.hamzajg.accounting.customer.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class CustomerDataMapper implements ExchangeMapper<CustomerData,String> {

  @Override
  public String localToExternal(final CustomerData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public CustomerData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, CustomerData.class);
  }
}
