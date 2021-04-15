package com.hamzajg.accounting.bank.model.journal;

import com.hamzajg.accounting.bank.model.*;

public final class JournalState {

  public final String id;
  public final String date;
  public final String description;
  public final JournalLine journalLines;

  public static JournalState identifiedBy(final String id) {
    return new JournalState(id, null, null, null);
  }

  public JournalState (final String id, final String date, final String description, final JournalLine journalLines) {
    this.id = id;
    this.date = date;
    this.description = description;
    this.journalLines = journalLines;
  }

  public JournalState create(final String date, final String description, final JournalLine journalLines) {
    return new JournalState(this.id, date, description, journalLines);
  }

  public JournalState changeDate(final String date) {
    return new JournalState(this.id, date, this.description, this.journalLines);
  }

  public JournalState changeDescription(final String description) {
    return new JournalState(this.id, this.date, description, this.journalLines);
  }

  public JournalState addJournalLines(final JournalLine journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

  public JournalState removeJournalLines(final JournalLine journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

  public JournalState changeJouralLines(final JournalLine journalLines) {
    return new JournalState(this.id, this.date, this.description, journalLines);
  }

}
