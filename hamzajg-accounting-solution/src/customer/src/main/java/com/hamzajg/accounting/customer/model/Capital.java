package com.hamzajg.accounting.customer.model;

public final class Capital {

  public final int value;

  public static Capital from(final int value) {
    return new Capital(value);
  }

  private Capital (final int value) {
    this.value = value;
  }

}