package com.hamzajg.accounting.bank.infrastructure;

import com.hamzajg.accounting.bank.model.Money;

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