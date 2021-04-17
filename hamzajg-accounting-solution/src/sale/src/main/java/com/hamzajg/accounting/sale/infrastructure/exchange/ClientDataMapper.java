package com.hamzajg.accounting.sale.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.sale.infrastructure.ClientData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ClientDataMapper implements ExchangeMapper<ClientData,String> {

  @Override
  public String localToExternal(final ClientData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public ClientData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, ClientData.class);
  }
}
