package com.hamzajg.accounting.bank.model.journal;

import com.hamzajg.accounting.bank.model.*;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

import java.util.Set;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class JournalEntity extends EventSourced implements Journal {
  private JournalState state;

  public JournalEntity(final String id) {
    super(id);
    this.state = JournalState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(JournalEntity.class, JournalCreated.class, JournalEntity::applyJournalCreated);
    EventSourced.registerConsumer(JournalEntity.class, JournalDateChanged.class, JournalEntity::applyJournalDateChanged);
    EventSourced.registerConsumer(JournalEntity.class, JournalDescriptionChanged.class, JournalEntity::applyJournalDescriptionChanged);
    EventSourced.registerConsumer(JournalEntity.class, JournalLinesAdded.class, JournalEntity::applyJournalLinesAdded);
    EventSourced.registerConsumer(JournalEntity.class, JournalLinesRemoved.class, JournalEntity::applyJournalLinesRemoved);
    EventSourced.registerConsumer(JournalEntity.class, JournalLinesChanged.class, JournalEntity::applyJournalLinesChanged);
  }

  @Override
  public Completes<JournalState> create(final String date, final String description, final Set<JournalLine> journalLines) {
    final JournalState stateArg = state.create(date, description, journalLines);
    return apply(new JournalCreated(stateArg), () -> state);
  }

  @Override
  public Completes<JournalState> changeDate(final String date) {
    final JournalState stateArg = state.changeDate(date);
    return apply(new JournalDateChanged(stateArg), () -> state);
  }

  @Override
  public Completes<JournalState> changeDescription(final String description) {
    final JournalState stateArg = state.changeDescription(description);
    return apply(new JournalDescriptionChanged(stateArg), () -> state);
  }

  @Override
  public Completes<JournalState> addJournalLines(final Set<JournalLine> journalLines) {
    final JournalState stateArg = state.addJournalLines(journalLines);
    return apply(new JournalLinesAdded(stateArg), () -> state);
  }

  @Override
  public Completes<JournalState> removeJournalLines(final Set<JournalLine> journalLines) {
    final JournalState stateArg = state.removeJournalLines(journalLines);
    return apply(new JournalLinesRemoved(stateArg), () -> state);
  }

  @Override
  public Completes<JournalState> changeJournalLines(final Set<JournalLine> journalLines) {
    final JournalState stateArg = state.changeJournalLines(journalLines);
    return apply(new JournalLinesChanged(stateArg), () -> state);
  }

  private void applyJournalCreated(final JournalCreated event) {
    state = state.create(event.date, event.description, event.journalLines);
  }

  private void applyJournalDateChanged(final JournalDateChanged event) {
    state = state.changeDate(event.date);
  }

  private void applyJournalDescriptionChanged(final JournalDescriptionChanged event) {
    state = state.changeDescription(event.description);
  }

  private void applyJournalLinesAdded(final JournalLinesAdded event) {
    state = state.addJournalLines(event.journalLines);
  }

  private void applyJournalLinesRemoved(final JournalLinesRemoved event) {
    state = state.removeJournalLines(event.journalLines);
  }

  private void applyJournalLinesChanged(final JournalLinesChanged event) {
    state = state.changeJournalLines(event.journalLines);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code JournalState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <JournalState> void restoreSnapshot(final JournalState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code JournalState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return JournalState
   */
  @Override
  protected JournalState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
