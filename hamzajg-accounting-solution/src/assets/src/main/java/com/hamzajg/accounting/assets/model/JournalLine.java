package com.hamzajg.accounting.assets.model;

public final class JournalLine {

  public final String id;
  public final Money credit;
  public final Money debit;
  public final String description;

  public static JournalLine from(final String id, final Money credit, final Money debit, final String description) {
    return new JournalLine(id, credit, debit, description);
  }

  private JournalLine(final String id, final Money credit, final Money debit, final String description) {
    this.id = id;
    this.credit = credit;
    this.debit = debit;
    this.description = description;
  }

}