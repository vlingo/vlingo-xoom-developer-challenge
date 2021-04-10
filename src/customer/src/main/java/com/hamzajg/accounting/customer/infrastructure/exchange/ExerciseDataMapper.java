package com.hamzajg.accounting.customer.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ExerciseDataMapper implements ExchangeMapper<ExerciseData,String> {

  @Override
  public String localToExternal(final ExerciseData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public ExerciseData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, ExerciseData.class);
  }
}
