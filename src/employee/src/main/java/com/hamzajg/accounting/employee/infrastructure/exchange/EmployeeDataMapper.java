package com.hamzajg.accounting.employee.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

import com.hamzajg.accounting.employee.infrastructure.EmployeeData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class EmployeeDataMapper implements ExchangeMapper<EmployeeData,String> {

  @Override
  public String localToExternal(final EmployeeData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public EmployeeData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, EmployeeData.class);
  }
}
