package com.hamzajg.accounting.customer.infrastructure;

import com.hamzajg.accounting.customer.model.Capital;

public class CapitalData {

  public final int value;

  public static CapitalData from(final Capital capital) {
    return from(capital.value);
  }

  public static CapitalData from(final int value) {
    return new CapitalData(value);
  }

  private CapitalData (final int value) {
    this.value = value;
  }

}