package com.hamzajg.accounting.customer.model.exercise;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

import java.time.LocalDate;

public interface Exercise {
    static Completes<ExerciseState> create(Stage stage, final LocalDate startDate, final LocalDate endDate,
                                           final String customerId) {
        Address address = stage.addressFactory().uniquePrefixedWith("g-");
        Exercise exercise = stage.actorFor(Exercise.class,
                Definition.has(ExerciseEntity.class, Definition.parameters(address.idString())), address);
        return exercise.create(startDate, endDate, customerId);
    }

    Completes<ExerciseState> create(final LocalDate startDate, final LocalDate endDate, final String customerId);

    Completes<ExerciseState> close(final LocalDate closetAt, final boolean isClosed);

    Completes<ExerciseState> changeEndDate(final LocalDate endDate);

    Completes<ExerciseState> changeStartDate(final LocalDate startDate);

}