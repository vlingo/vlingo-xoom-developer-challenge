package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import com.hamzajg.accounting.bank.model.*;
import com.hamzajg.accounting.bank.infrastructure.JournalData;
import com.hamzajg.accounting.bank.model.journal.JournalEntity;
import com.hamzajg.accounting.bank.model.journal.Journal;

public class JournalExchangeReceivers {

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalCreated implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalCreated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
      final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
      final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
      Journal.create(stage, data.date, data.description, journalLines);
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalDateChanged implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalDateChanged(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(journal -> journal.changeDate(data.date));
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalDescriptionChanged implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalDescriptionChanged(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(journal -> journal.changeDescription(data.description));
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalLinesAdded implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalLinesAdded(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
      final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
      final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
      stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(journal -> journal.addJournalLines(journalLines));
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalLinesRemoved implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalLinesRemoved(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
      final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
      final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
      stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(journal -> journal.removeJournalLines(journalLines));
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class JournalLinesChanged implements ExchangeReceiver<JournalData> {

    private final Grid stage;

    public JournalLinesChanged(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final JournalData data) {
      final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
      final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
      final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
      stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(journal -> journal.changeJouralLines(journalLines));
    }
  }

}
