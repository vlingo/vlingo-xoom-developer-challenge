package com.hamzajg.accounting.rental.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.rental.infrastructure.RentalContractData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class RentalContractDataMapper implements ExchangeMapper<RentalContractData,String> {

  @Override
  public String localToExternal(final RentalContractData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public RentalContractData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, RentalContractData.class);
  }
}
