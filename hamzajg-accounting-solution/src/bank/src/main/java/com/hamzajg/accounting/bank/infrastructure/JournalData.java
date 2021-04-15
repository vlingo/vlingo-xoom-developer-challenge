package com.hamzajg.accounting.bank.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import com.hamzajg.accounting.bank.model.journal.JournalState;

public class JournalData {
  public final String id;
  public final String date;
  public final String description;
  public final JournalLineData journalLines;

  public static JournalData from(final JournalState journalState) {
    final JournalLineData journalLines = journalState.journalLines != null ? JournalLineData.from(journalState.journalLines) : null;
    return from(journalState.id, journalState.date, journalState.description, journalLines);
  }

  public static JournalData from(final String id, final String date, final String description, final JournalLineData journalLines) {
    return new JournalData(id, date, description, journalLines);
  }

  public static List<JournalData> from(final List<JournalState> states) {
    return states.stream().map(JournalData::from).collect(Collectors.toList());
  }

  public static JournalData empty() {
    return from(JournalState.identifiedBy(""));
  }

  private JournalData (final String id, final String date, final String description, final JournalLineData journalLines) {
    this.id = id;
    this.date = date;
    this.description = description;
    this.journalLines = journalLines;
  }

}
