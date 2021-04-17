package com.hamzajg.accounting.bank.model;

public final class JournalLine {

  public final String bankAccountId;
  public final Money credit;
  public final Money debit;
  public final String clientId;

  public static JournalLine from(final String bankAccountId, final Money credit, final Money debit, final String clientId) {
    return new JournalLine(bankAccountId, credit, debit, clientId);
  }

  private JournalLine (final String bankAccountId, final Money credit, final Money debit, final String clientId) {
    this.bankAccountId = bankAccountId;
    this.credit = credit;
    this.debit = debit;
    this.clientId = clientId;
  }

}