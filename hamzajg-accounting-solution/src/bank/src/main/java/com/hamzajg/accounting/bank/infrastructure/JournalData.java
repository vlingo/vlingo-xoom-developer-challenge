package com.hamzajg.accounting.bank.infrastructure;

import com.hamzajg.accounting.bank.model.journal.JournalState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JournalData {
    public final String id;
    public final String date;
    public final String description;
    public final Set<JournalLineData> journalLines;

    public static JournalData from(final JournalState journalState) {
        final Set<JournalLineData> journalLines = journalState.journalLines != null ?
                journalState.journalLines.stream().map(JournalLineData::from).collect(Collectors.toSet()) : new HashSet<>();
        return from(journalState.id, journalState.date, journalState.description, journalLines);
    }

    public static JournalData from(final String id, final String date, final String description, final Set<JournalLineData> journalLines) {
        return new JournalData(id, date, description, journalLines);
    }

    public static List<JournalData> from(final List<JournalState> states) {
        return states.stream().map(JournalData::from).collect(Collectors.toList());
    }

    public static JournalData empty() {
        return from(JournalState.identifiedBy(""));
    }

    private JournalData(final String id, final String date, final String description, final Set<JournalLineData> journalLines) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.journalLines = journalLines;
    }

}
