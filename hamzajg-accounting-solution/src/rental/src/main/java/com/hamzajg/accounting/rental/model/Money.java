package com.hamzajg.accounting.rental.model;

public final class Money {

  public final double amount;
  public final String currency;

  public static Money from(final double amount, final String currency) {
    return new Money(amount, currency);
  }

  private Money (final double amount, final String currency) {
    this.amount = amount;
    this.currency = currency;
  }

}