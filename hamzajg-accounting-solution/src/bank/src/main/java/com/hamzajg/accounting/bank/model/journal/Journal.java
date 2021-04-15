package com.hamzajg.accounting.bank.model.journal;

import io.vlingo.xoom.actors.Definition;
import com.hamzajg.accounting.bank.model.*;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Journal {

  Completes<JournalState> create(final String date, final String description, final JournalLine journalLines);

  static Completes<JournalState> create(final Stage stage, final String date, final String description, final JournalLine journalLines) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Journal _journal = stage.actorFor(Journal.class, Definition.has(JournalEntity.class, Definition.parameters(_address.idString())), _address);
    return _journal.create(date, description, journalLines);
  }

  Completes<JournalState> changeDate(final String date);

  Completes<JournalState> changeDescription(final String description);

  Completes<JournalState> addJournalLines(final JournalLine journalLines);

  Completes<JournalState> removeJournalLines(final JournalLine journalLines);

  Completes<JournalState> changeJouralLines(final JournalLine journalLines);

}