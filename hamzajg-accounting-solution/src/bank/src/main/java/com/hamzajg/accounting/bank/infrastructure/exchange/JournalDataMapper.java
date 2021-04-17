package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.bank.infrastructure.JournalData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class JournalDataMapper implements ExchangeMapper<JournalData,String> {

  @Override
  public String localToExternal(final JournalData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public JournalData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, JournalData.class);
  }
}
