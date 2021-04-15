package com.hamzajg.accounting.assets.infrastructure.exchange;

import com.hamzajg.accounting.assets.infrastructure.JournalData;
import com.hamzajg.accounting.assets.model.JournalLine;
import com.hamzajg.accounting.assets.model.Money;
import com.hamzajg.accounting.assets.model.journal.Journal;
import com.hamzajg.accounting.assets.model.journal.JournalEntity;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
            final Set<JournalLine> journalLines = data.journalLines.stream().map(item -> JournalLine.from(item.id,  Money.from(item.credit.amount, item.credit.currency), Money.from(item.debit.amount, item.debit.currency), item.description)).collect(Collectors.toSet());
            System.err.println("receive - JournalCreated - JournalExchangeReceivers");
            Journal.create(stage, LocalDate.parse(data.date), data.type, data.title, data.exerciseId, journalLines);
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
                    .andFinallyConsume(journal -> journal.changeDate(LocalDate.parse(data.date)));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class JournalTypeChanged implements ExchangeReceiver<JournalData> {

        private final Grid stage;

        public JournalTypeChanged(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final JournalData data) {
            stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(journal -> journal.changeType(data.type));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class JournalTitleChanged implements ExchangeReceiver<JournalData> {

        private final Grid stage;

        public JournalTitleChanged(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final JournalData data) {
            stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(journal -> journal.changeTitle(data.title));
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
            final Set<JournalLine> journalLines = data.journalLines.stream().map(item -> JournalLine.from(item.id,  Money.from(item.credit.amount, item.credit.currency), Money.from(item.debit.amount, item.debit.currency), item.description)).collect(Collectors.toSet());
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
            final Set<JournalLine> journalLines = data.journalLines.stream().map(item -> JournalLine.from(item.id,  Money.from(item.credit.amount, item.credit.currency), Money.from(item.debit.amount, item.debit.currency), item.description)).collect(Collectors.toSet());
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
            final Set<JournalLine> journalLines = data.journalLines.stream().map(item -> JournalLine.from(item.id,  Money.from(item.credit.amount, item.credit.currency), Money.from(item.debit.amount, item.debit.currency), item.description)).collect(Collectors.toSet());
            stage.actorOf(Journal.class, stage.addressFactory().from(data.id), Definition.has(JournalEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(journal -> journal.changeJournalLine(journalLines));
        }
    }

}
