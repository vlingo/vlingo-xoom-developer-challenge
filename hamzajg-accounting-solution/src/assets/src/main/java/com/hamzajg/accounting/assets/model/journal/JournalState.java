package com.hamzajg.accounting.assets.model.journal;

import com.hamzajg.accounting.assets.model.JournalLine;
import io.vlingo.xoom.symbio.store.object.StateObject;

import java.util.Set;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object
 * Storage</a>
 */
public final class JournalState extends StateObject {

    public final String id;
    public final String date;
    public final String type;
    public final String title;
    public final String exerciseId;
    public final Set<JournalLine> journalLines;

    public static JournalState identifiedBy(final String id) {
        return new JournalState(id, null, null, null, null, null);
    }

    public JournalState(final String id, final String date, final String type, final String title,
            final String exerciseId, final Set<JournalLine> journalLines) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.title = title;
        this.exerciseId = exerciseId;
        this.journalLines = journalLines;
    }

    public JournalState create(final String date, final String type, final String title, final String exerciseId,
            final Set<JournalLine> journalLines) {
        return new JournalState(this.id, date, type, title, exerciseId, journalLines);
    }

    public JournalState changeDate(final String date) {
        return new JournalState(this.id, date, this.type, this.title, this.exerciseId, this.journalLines);
    }

    public JournalState changeType(final String type) {
        return new JournalState(this.id, this.date, type, this.title, this.exerciseId, this.journalLines);
    }

    public JournalState changeTitle(final String title) {
        return new JournalState(this.id, this.date, this.type, title, this.exerciseId, this.journalLines);
    }

    public JournalState addJournalLines(final Set<JournalLine> journalLines) {
        return new JournalState(this.id, this.date, this.type, this.title, this.exerciseId, journalLines);
    }

    public JournalState removeJournalLines(final Set<JournalLine> journalLines) {
        return new JournalState(this.id, this.date, this.type, this.title, this.exerciseId, journalLines);
    }

    public JournalState changeJournalLine(final Set<JournalLine> journalLines) {
        return new JournalState(this.id, this.date, this.type, this.title, this.exerciseId, journalLines);
    }

}
