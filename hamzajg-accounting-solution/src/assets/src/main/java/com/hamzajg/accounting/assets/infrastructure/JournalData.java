package com.hamzajg.accounting.assets.infrastructure;

import com.hamzajg.accounting.assets.model.journal.JournalState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JournalData {
    public final String id;
    public final String date;
    public final String type;
    public final String title;
    public final String exerciseId;
    public final Set<JournalLineData> journalLines;

    public static JournalData from(final JournalState journalState) {
        final Set<JournalLineData> journalLines = journalState.journalLines != null ? journalState.journalLines.stream().map(JournalLineData::from).collect(Collectors.toSet()) : null;
        return from(journalState.id, journalState.date, journalState.type, journalState.title, journalState.exerciseId, journalLines);
    }

    public static JournalData from(final String id, final String date, final String type, final String title,
                                   final String exerciseId, final Set<JournalLineData> journalLines) {
        return new JournalData(id, date, type, title, exerciseId, journalLines);
    }

    public static List<JournalData> from(final List<JournalState> states) {
        return states.stream().map(JournalData::from).collect(Collectors.toList());
    }

    public static JournalData empty() {
        return from(JournalState.identifiedBy(""));
    }

    private JournalData(final String id, final String date, final String type, final String title, final String exerciseId,
                        final Set<JournalLineData> journalLines) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.title = title;
        this.exerciseId = exerciseId;
        this.journalLines = journalLines;
    }

}
