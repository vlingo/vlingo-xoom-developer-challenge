package com.hamzajg.accounting.bank.infrastructure;

import com.hamzajg.accounting.bank.model.JournalLine;

public class JournalLineData {

  public final String bankAccountId;
  public final MoneyData credit;
  public final MoneyData debit;
  public final String clientId;

  public static JournalLineData from(final JournalLine journalLine) {
    final MoneyData credit = journalLine.credit != null ? MoneyData.from(journalLine.credit) : null;
    final MoneyData debit = journalLine.debit != null ? MoneyData.from(journalLine.debit) : null;
    return from(journalLine.bankAccountId, credit, debit, journalLine.clientId);
  }

  public static JournalLineData from(final String bankAccountId, final MoneyData credit, final MoneyData debit, final String clientId) {
    return new JournalLineData(bankAccountId, credit, debit, clientId);
  }

  private JournalLineData (final String bankAccountId, final MoneyData credit, final MoneyData debit, final String clientId) {
    this.bankAccountId = bankAccountId;
    this.credit = credit;
    this.debit = debit;
    this.clientId = clientId;
  }

}