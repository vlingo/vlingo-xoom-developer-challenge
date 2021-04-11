package com.hamzajg.accounting.customer.infrastructure;

import com.hamzajg.accounting.customer.model.exercise.ExerciseState;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseData {
    public final String id;
    public final String startDate;
    public final String endDate;
    public final CustomerData customer;
    public final String closedAt;
    public final boolean isClosed;

    public static ExerciseData from(final ExerciseState exerciseState) {
        return from(exerciseState.id, exerciseState.startDate.toString(), exerciseState.endDate.toString(), CustomerData.from(exerciseState.customer));
    }

    public static ExerciseData from(final String id, final String startDate, final String endDate, final CustomerData customer) {
        return new ExerciseData(id, startDate, endDate, customer);
    }

    public static List<ExerciseData> from(final List<ExerciseState> states) {
        return states.stream().map(ExerciseData::from).collect(Collectors.toList());
    }

    public static ExerciseData empty() {
        return from(ExerciseState.identifiedBy(""));
    }

    private ExerciseData(final String id, final String startDate, final String endDate, final CustomerData customer) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedAt = null;
        this.customer = customer;
        this.isClosed = false;
    }

    private ExerciseData(final String id, final String startDate, final String endDate, final String closedAt,
                         final boolean isClosed, final CustomerData customer) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedAt = closedAt;
        this.customer = customer;
        this.isClosed = isClosed;
    }

    public static ExerciseData from(String id, String startDate, String endDate, String closedAt,
                                    boolean isClosed, final CustomerData customer) {
        return new ExerciseData(id, startDate, endDate, closedAt, isClosed, customer);
    }
}
