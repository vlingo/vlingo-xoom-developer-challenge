package com.hamzajg.accounting.assets.model.journal;

import com.hamzajg.accounting.assets.model.JournalLine;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

import java.time.LocalDate;
import java.util.Set;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class JournalEntity extends StatefulEntity<JournalState> implements Journal {
    private JournalState state;

    public JournalEntity(final String id) {
        super(id);
        this.state = JournalState.identifiedBy(id);
    }

    @Override
    public Completes<JournalState> create(final LocalDate date, final String type, final String title, final String exerciseId, final Set<JournalLine> journalLines) {
        final JournalState stateArg = state.create(date.toString(), type, title, exerciseId, journalLines);
        return apply(stateArg, new JournalCreated(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> changeDate(final LocalDate date) {
        final JournalState stateArg = state.changeDate(date.toString());
        return apply(stateArg, new JournalDateChanged(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> changeType(final String type) {
        final JournalState stateArg = state.changeType(type);
        return apply(stateArg, new JournalTypeChanged(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> changeTitle(final String title) {
        final JournalState stateArg = state.changeTitle(title);
        return apply(stateArg, new JournalTitleChanged(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> addJournalLines(final Set<JournalLine> journalLines) {
        final JournalState stateArg = state.addJournalLines(journalLines);
        return apply(stateArg, new JournalLineAdded(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> removeJournalLines(final Set<JournalLine> journalLines) {
        final JournalState stateArg = state.removeJournalLines(journalLines);
        return apply(stateArg, new JournalLineRemoved(stateArg), () -> state);
    }

    @Override
    public Completes<JournalState> changeJournalLine(final Set<JournalLine> journalLines) {
        final JournalState stateArg = state.changeJournalLine(journalLines);
        return apply(stateArg, new JournalLineChanged(stateArg), () -> state);
    }

    /*
     * Received when my current state has been applied and restored.
     *
     * @param state the JournalState
     */
    @Override
    protected void state(final JournalState state) {
        this.state = state;
    }

    /*
     * Received when I must provide my state type.
     *
     * @return {@code Class<JournalState>}
     */
    @Override
    protected Class<JournalState> stateType() {
        return JournalState.class;
    }
}
