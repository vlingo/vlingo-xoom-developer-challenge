package com.hamzajg.accounting.customer.infrastructure.exchange;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import com.hamzajg.accounting.customer.model.exercise.Exercise;
import com.hamzajg.accounting.customer.model.exercise.ExerciseEntity;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import java.time.LocalDate;

public class ExerciseExchangeReceivers {

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class ExerciseCreated implements ExchangeReceiver<ExerciseData> {

        private final Grid stage;

        public ExerciseCreated(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final ExerciseData data) {
            stage.actorOf(Exercise.class, stage.addressFactory().from(data.id), Definition.has(ExerciseEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(exercise -> exercise.create(LocalDate.parse(data.startDate),
                            LocalDate.parse(data.endDate), data.customer.id));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class ExerciseClosed implements ExchangeReceiver<ExerciseData> {

        private final Grid stage;

        public ExerciseClosed(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final ExerciseData data) {
            stage.actorOf(Exercise.class, stage.addressFactory().from(data.id), Definition.has(ExerciseEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(exercise -> exercise.close(LocalDate.parse(data.closedAt), data.isClosed));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class ExerciseStartDateChanged implements ExchangeReceiver<ExerciseData> {

        private final Grid stage;

        public ExerciseStartDateChanged(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final ExerciseData data) {
            stage.actorOf(Exercise.class, stage.addressFactory().from(data.id), Definition.has(ExerciseEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(exercise -> exercise.changeStartDate(LocalDate.parse(data.startDate)));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class ExerciseEndDateChanged implements ExchangeReceiver<ExerciseData> {

        private final Grid stage;

        public ExerciseEndDateChanged(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final ExerciseData data) {
            stage.actorOf(Exercise.class, stage.addressFactory().from(data.id), Definition.has(ExerciseEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(exercise -> exercise.changeEndDate(LocalDate.parse(data.endDate)));
        }
    }

}
