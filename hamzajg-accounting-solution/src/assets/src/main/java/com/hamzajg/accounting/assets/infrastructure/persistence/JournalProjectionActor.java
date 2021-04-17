package com.hamzajg.accounting.assets.infrastructure.persistence;

import com.hamzajg.accounting.assets.infrastructure.Events;
import com.hamzajg.accounting.assets.infrastructure.JournalData;
import com.hamzajg.accounting.assets.infrastructure.JournalLineData;
import com.hamzajg.accounting.assets.infrastructure.MoneyData;
import com.hamzajg.accounting.assets.model.journal.*;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 * StateStoreProjectionActor
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
    protected JournalData merge(final JournalData previousData, final int previousVersion, final JournalData currentData,
                                final int currentVersion) {

        if (previousVersion == currentVersion) return currentData;

        JournalData merged = previousData;

        for (final Source<?> event : sources()) {
            switch (Events.valueOf(event.typeName())) {
                case JournalCreated: {
                    final JournalCreated typedEvent = typed(event);
                    merged = JournalData.from(typedEvent.id, typedEvent.date, typedEvent.type, typedEvent.title, typedEvent.exerciseId, null);
                    break;
                }

                case JournalDateChanged: {
                    final JournalDateChanged typedEvent = typed(event);
                    merged = JournalData.from(typedEvent.id, typedEvent.date, previousData.type, previousData.title,
                            previousData.exerciseId, previousData.journalLines);
                    break;
                }

                case JournalTypeChanged: {
                    final JournalTypeChanged typedEvent = typed(event);
                    merged = JournalData.from(typedEvent.id, previousData.date, typedEvent.type, previousData.title,
                            previousData.exerciseId, previousData.journalLines);
                    break;
                }

                case JournalTitleChanged: {
                    final JournalTitleChanged typedEvent = typed(event);
                    merged = JournalData.from(typedEvent.id, previousData.date, previousData.type, typedEvent.title,
                            previousData.exerciseId, previousData.journalLines);
                    break;
                }

                case JournalLineAdded: {
                    final JournalLineAdded typedEvent = typed(event);
                    final Set<JournalLineData> journalLines = typedEvent.journalLines.stream()
                            .map(item -> JournalLineData.from(item.id, item.clientId, item.vendorId, MoneyData.from(item.credit.amount, item.credit.currency),
                                    MoneyData.from(item.debit.amount, item.debit.currency), item.description))
                            .collect(Collectors.toSet());
                    merged = JournalData.from(typedEvent.id, previousData.date, previousData.type, previousData.title, previousData.exerciseId, journalLines);
                    break;
                }

                case JournalLineRemoved: {
                    final JournalLineRemoved typedEvent = typed(event);
                    final Set<JournalLineData> journalLines = typedEvent.journalLines.stream()
                            .map(item -> JournalLineData.from(item.id, item.clientId, item.vendorId, MoneyData.from(item.credit.amount, item.credit.currency),
                                    MoneyData.from(item.debit.amount, item.debit.currency), item.description))
                            .collect(Collectors.toSet());
                    merged = JournalData.from(typedEvent.id, previousData.date, previousData.type, previousData.title, previousData.exerciseId, journalLines);
                    break;
                }

                case JournalLineChanged: {
                    final JournalLineChanged typedEvent = typed(event);
                    final Set<JournalLineData> journalLines = typedEvent.journalLines.stream()
                            .map(item -> JournalLineData.from(item.id, item.clientId, item.vendorId, MoneyData.from(item.credit.amount, item.credit.currency),
                                    MoneyData.from(item.debit.amount, item.debit.currency), item.description))
                            .collect(Collectors.toSet());
                    merged = JournalData.from(typedEvent.id, previousData.date, previousData.type, previousData.title, previousData.exerciseId, journalLines);
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
