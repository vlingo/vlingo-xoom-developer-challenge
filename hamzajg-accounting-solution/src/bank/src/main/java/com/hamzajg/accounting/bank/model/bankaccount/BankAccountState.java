package com.hamzajg.accounting.bank.model.bankaccount;

import com.hamzajg.accounting.bank.model.*;

public final class BankAccountState {

  public final String id;
  public final String rib;
  public final String iban;
  public final String type;
  public final String bicCode;
  public final Money balance;
  public final String agency;

  public static BankAccountState identifiedBy(final String id) {
    return new BankAccountState(id, null, null, null, null, null, null);
  }

  public BankAccountState (final String id, final String rib, final String iban, final String type, final String bicCode, final Money balance, final String agency) {
    this.id = id;
    this.rib = rib;
    this.iban = iban;
    this.type = type;
    this.bicCode = bicCode;
    this.balance = balance;
    this.agency = agency;
  }

  public BankAccountState create(final String rib, final String iban, final String type, final String bicCode, final Money balance, final String agency) {
    return new BankAccountState(this.id, rib, iban, type, bicCode, balance, agency);
  }

}
