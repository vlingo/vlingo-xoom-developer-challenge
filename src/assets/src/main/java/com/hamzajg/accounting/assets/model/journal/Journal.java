package com.hamzajg.accounting.assets.model.journal;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import com.hamzajg.accounting.assets.model.*;
import io.vlingo.xoom.common.Completes;

public interface Journal {

  Completes<JournalState> create(final String date, final String type, final String title, final JournalLine journalLines);

  static Completes<JournalState> create(final Stage stage, final String date, final String type, final String title, final JournalLine journalLines) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Journal _journal = stage.actorFor(Journal.class, Definition.has(JournalEntity.class, Definition.parameters(_address.idString())), _address);
    return _journal.create(date, type, title, journalLines);
  }

  Completes<JournalState> changeDate(final String date);

  Completes<JournalState> changeType(final String type);

  Completes<JournalState> changeTitle(final String title);

  Completes<JournalState> addJournalLines(final JournalLine journalLines);

  Completes<JournalState> removeJournalLines(final JournalLine journalLines);

  Completes<JournalState> changeJournalLine(final JournalLine journalLines);

}