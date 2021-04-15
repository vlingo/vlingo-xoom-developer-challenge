package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.journal.*;
import com.hamzajg.accounting.bank.infrastructure.*;
import com.hamzajg.accounting.bank.infrastructure.Events;

import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class JournalProjectionActor extends StateStoreProjectionActor<JournalData> {

  private static final JournalData Empty = JournalData.empty();

  public JournalProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public JournalProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected JournalData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected JournalData merge(final JournalData previousData, final int previousVersion, final JournalData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    JournalData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case JournalCreated: {
          final JournalCreated typedEvent = typed(event);
          final MoneyData credit = MoneyData.from(typedEvent.journalLines.credit.amount, typedEvent.journalLines.credit.currency);
          final MoneyData debit = MoneyData.from(typedEvent.journalLines.debit.amount, typedEvent.journalLines.debit.currency);
          final JournalLineData journalLines = JournalLineData.from(typedEvent.journalLines.bankAccountId, credit, debit, typedEvent.journalLines.clientId);
          merged = JournalData.from(typedEvent.id, typedEvent.date, typedEvent.description, journalLines);
          break;
        }

        case JournalDateChanged: {
          final JournalDateChanged typedEvent = typed(event);
          merged = JournalData.from(typedEvent.id, typedEvent.date, previousData.description, previousData.journalLines);
          break;
        }

        case JournalDescriptionChanged: {
          final JournalDescriptionChanged typedEvent = typed(event);
          merged = JournalData.from(typedEvent.id, previousData.date, typedEvent.description, previousData.journalLines);
          break;
        }

        case JournalLinesAdded: {
          final JournalLinesAdded typedEvent = typed(event);
          final MoneyData credit = MoneyData.from(typedEvent.journalLines.credit.amount, typedEvent.journalLines.credit.currency);
          final MoneyData debit = MoneyData.from(typedEvent.journalLines.debit.amount, typedEvent.journalLines.debit.currency);
          final JournalLineData journalLines = JournalLineData.from(typedEvent.journalLines.bankAccountId, credit, debit, typedEvent.journalLines.clientId);
          merged = JournalData.from(typedEvent.id, previousData.date, previousData.description, journalLines);
          break;
        }

        case JournalLinesRemoved: {
          final JournalLinesRemoved typedEvent = typed(event);
          final MoneyData credit = MoneyData.from(typedEvent.journalLines.credit.amount, typedEvent.journalLines.credit.currency);
          final MoneyData debit = MoneyData.from(typedEvent.journalLines.debit.amount, typedEvent.journalLines.debit.currency);
          final JournalLineData journalLines = JournalLineData.from(typedEvent.journalLines.bankAccountId, credit, debit, typedEvent.journalLines.clientId);
          merged = JournalData.from(typedEvent.id, previousData.date, previousData.description, journalLines);
          break;
        }

        case JournalLinesChanged: {
          final JournalLinesChanged typedEvent = typed(event);
          final MoneyData credit = MoneyData.from(typedEvent.journalLines.credit.amount, typedEvent.journalLines.credit.currency);
          final MoneyData debit = MoneyData.from(typedEvent.journalLines.debit.amount, typedEvent.journalLines.debit.currency);
          final JournalLineData journalLines = JournalLineData.from(typedEvent.journalLines.bankAccountId, credit, debit, typedEvent.journalLines.clientId);
          merged = JournalData.from(typedEvent.id, previousData.date, previousData.description, journalLines);
          break;
        }

        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return merged;
  }
}
