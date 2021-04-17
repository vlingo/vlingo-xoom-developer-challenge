package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class BankAccountDataMapper implements ExchangeMapper<BankAccountData,String> {

  @Override
  public String localToExternal(final BankAccountData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public BankAccountData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, BankAccountData.class);
  }
}
