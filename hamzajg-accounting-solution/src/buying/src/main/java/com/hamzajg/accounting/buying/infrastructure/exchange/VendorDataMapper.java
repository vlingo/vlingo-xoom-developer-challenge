package com.hamzajg.accounting.buying.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.buying.infrastructure.VendorData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class VendorDataMapper implements ExchangeMapper<VendorData,String> {

  @Override
  public String localToExternal(final VendorData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public VendorData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, VendorData.class);
  }
}
