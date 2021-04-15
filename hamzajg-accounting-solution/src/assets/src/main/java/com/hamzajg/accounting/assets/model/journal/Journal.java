package com.hamzajg.accounting.assets.model.journal;

import com.hamzajg.accounting.assets.model.JournalLine;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

import java.time.LocalDate;
import java.util.Set;

public interface Journal {

    Completes<JournalState> create(final LocalDate date, final String type, final String title, final String exerciseId, final Set<JournalLine> journalLines);

    static Completes<JournalState> create(final Stage stage, final LocalDate date, final String type, final String title, final String exerciseId, final Set<JournalLine> journalLines) {
        System.err.println("Journal - create");
        final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
        final Journal _journal = stage.actorFor(Journal.class, Definition.has(JournalEntity.class, Definition.parameters(_address.idString())), _address);
        return _journal.create(date, type, title, exerciseId, journalLines);
    }

    Completes<JournalState> changeDate(final LocalDate date);

    Completes<JournalState> changeType(final String type);

    Completes<JournalState> changeTitle(final String title);

    Completes<JournalState> addJournalLines(final Set<JournalLine> journalLines);

    Completes<JournalState> removeJournalLines(final Set<JournalLine> journalLines);

    Completes<JournalState> changeJournalLine(final Set<JournalLine> journalLines);

}