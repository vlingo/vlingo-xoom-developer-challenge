package com.hamzajg.accounting.bank.model.journal;

import com.hamzajg.accounting.bank.model.JournalLine;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

import java.util.Set;

public interface Journal {

    Completes<JournalState> create(final String date, final String description, final Set<JournalLine> journalLines);

    static Completes<JournalState> create(final Stage stage, final String date, final String description, final Set<JournalLine> journalLines) {
        final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
        final Journal _journal = stage.actorFor(Journal.class, Definition.has(JournalEntity.class, Definition.parameters(_address.idString())), _address);
        return _journal.create(date, description, journalLines);
    }

    Completes<JournalState> changeDate(final String date);

    Completes<JournalState> changeDescription(final String description);

    Completes<JournalState> addJournalLines(final Set<JournalLine> journalLines);

    Completes<JournalState> removeJournalLines(final Set<JournalLine> journalLines);

    Completes<JournalState> changeJournalLines(final Set<JournalLine> journalLines);

}