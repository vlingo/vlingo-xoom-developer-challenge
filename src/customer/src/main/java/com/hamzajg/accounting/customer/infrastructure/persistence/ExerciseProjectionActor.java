package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.infrastructure.Events;
import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import com.hamzajg.accounting.customer.model.exercise.ExerciseClosed;
import com.hamzajg.accounting.customer.model.exercise.ExerciseCreated;
import com.hamzajg.accounting.customer.model.exercise.ExerciseEndDateChanged;
import com.hamzajg.accounting.customer.model.exercise.ExerciseStartDateChanged;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 * StateStoreProjectionActor
 * </a>
 */
public class ExerciseProjectionActor extends StateStoreProjectionActor<ExerciseData> {

    private static final ExerciseData Empty = ExerciseData.empty();

    public ExerciseProjectionActor() {
        this(QueryModelStateStoreProvider.instance().store);
    }

    public ExerciseProjectionActor(final StateStore stateStore) {
        super(stateStore);
    }

    @Override
    protected ExerciseData currentDataFor(final Projectable projectable) {
        return Empty;
    }

    @Override
    protected ExerciseData merge(final ExerciseData previousData, final int previousVersion, final ExerciseData currentData, final int currentVersion) {

        if (previousVersion == currentVersion) return currentData;

        ExerciseData merged = previousData;

        for (final Source<?> event : sources()) {
            switch (Events.valueOf(event.typeName())) {
                case ExerciseCreated: {
                    final ExerciseCreated typedEvent = typed(event);
                    merged = ExerciseData.from(typedEvent.id, previousData.startDate, previousData.endDate, previousData.closedAt, previousData.isClosed, previousData.customer);
                    break;
                }

                case ExerciseClosed: {
                    final ExerciseClosed typedEvent = typed(event);
                    merged = ExerciseData.from(typedEvent.id, previousData.startDate, previousData.endDate, typedEvent.closetAt, typedEvent.isClosed, previousData.customer);
                    break;
                }

                case ExerciseEndDateChanged: {
                    final ExerciseEndDateChanged typedEvent = typed(event);
                    merged = ExerciseData.from(typedEvent.id, previousData.startDate, typedEvent.endDate, previousData.closedAt, previousData.isClosed, previousData.customer);
                    break;
                }

                case ExerciseStartDateChanged: {
                    final ExerciseStartDateChanged typedEvent = typed(event);
                    merged = ExerciseData.from(typedEvent.id, typedEvent.startDate, previousData.endDate, previousData.closedAt, previousData.isClosed, previousData.customer);
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
