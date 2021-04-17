package com.hamzajg.accounting.assets.infrastructure;

import com.hamzajg.accounting.assets.model.Money;

public class MoneyData {

  public final double amount;
  public final String currency;

  public static MoneyData from(final Money money) {
    return from(money.amount, money.currency);
  }

  public static MoneyData from(final double amount, final String currency) {
    return new MoneyData(amount, currency);
  }

  private MoneyData (final double amount, final String currency) {
    this.amount = amount;
    this.currency = currency;
  }

}