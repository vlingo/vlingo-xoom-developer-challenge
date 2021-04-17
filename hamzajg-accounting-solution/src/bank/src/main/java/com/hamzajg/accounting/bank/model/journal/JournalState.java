package com.hamzajg.accounting.bank.model.journal;

import com.hamzajg.accounting.bank.model.*;

import java.util.Set;

public final class JournalState {

  public final String id;
  public final String date;
  public final String description;
  public final Set<JournalLine> journalLines;

  public static JournalState identifiedBy(final String id) {
    return new JournalState(id, null, null, null);
  }

  public JournalState (final String id, final String date, final String description, final Set<JournalLine> journalLines) {
    this.id = id;
    this.date = date;
    this.description = description;
    this.journalLines = journalLines;
  }

  public JournalState create(final String date, final String description, final Set<JournalLine> journalLines) {
    return new JournalState(this.id, date, description, journalLines);
  }

  public JournalState changeDate(final String date) {
    return new JournalState(this.id, date, this.description, this.journalLines);
  }

  public JournalState changeDescription(final String description) {
    return new JournalState(this.id, this.date, description, this.journalLines);
  }

  public JournalState addJournalLines(final Set<JournalLine> journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

  public JournalState removeJournalLines(final Set<JournalLine> journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

  public JournalState changeJournalLines(final Set<JournalLine> journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

}
