package com.hamzajg.accounting.customer.model.exercise;

import com.hamzajg.accounting.customer.model.customer.CustomerState;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

import java.time.LocalDate;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class ExerciseEntity extends StatefulEntity<ExerciseState> implements Exercise {
    private ExerciseState state;

    public ExerciseEntity(final String id) {
        super(id);
        this.state = ExerciseState.identifiedBy(id);
    }

    @Override
    public Completes<ExerciseState> create(final LocalDate startDate, final LocalDate endDate, final String customerId) {
        final ExerciseState stateArg = state.create(startDate, endDate, CustomerState.identifiedBy(customerId));
        return apply(stateArg, new ExerciseCreated(stateArg), () -> state);
    }

    @Override
    public Completes<ExerciseState> close(final LocalDate closetAt, final boolean isClosed) {
        final ExerciseState stateArg = state.close(closetAt, isClosed);
        return apply(stateArg, new ExerciseClosed(stateArg), () -> state);
    }

    @Override
    public Completes<ExerciseState> changeEndDate(final LocalDate endDate) {
        final ExerciseState stateArg = state.changeEndDate(endDate);
        return apply(stateArg, new ExerciseEndDateChanged(stateArg), () -> state);
    }

    @Override
    public Completes<ExerciseState> changeStartDate(final LocalDate startDate) {
        final ExerciseState stateArg = state.changeStartDate(startDate);
        return apply(stateArg, new ExerciseStartDateChanged(stateArg), () -> state);
    }

    /*
     * Received when my current state has been applied and restored.
     *
     * @param state the ExerciseState
     */
    @Override
    protected void state(final ExerciseState state) {
        this.state = state;
    }

    /*
     * Received when I must provide my state type.
     *
     * @return {@code Class<ExerciseState>}
     */
    @Override
    protected Class<ExerciseState> stateType() {
        return ExerciseState.class;
    }
}
