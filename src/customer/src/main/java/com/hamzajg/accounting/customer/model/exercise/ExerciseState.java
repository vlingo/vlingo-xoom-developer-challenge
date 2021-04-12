package com.hamzajg.accounting.customer.model.exercise;

import com.hamzajg.accounting.customer.model.customer.CustomerState;
import io.vlingo.xoom.symbio.store.object.StateObject;

import java.time.LocalDate;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object Storage</a>
 */
public final class ExerciseState extends StateObject {

    public final String id;
    public final String startDate;
    public final String endDate;
    public final String closetAt;
    public final boolean isClosed;
    public final CustomerState customer;

    public static ExerciseState identifiedBy(final String id) {
        return new ExerciseState(id, null, null, null, false, null);
    }

    public ExerciseState(final String id, final String startDate, final String endDate, final String closetAt,
                         final boolean isClosed, final CustomerState customer) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closetAt = closetAt;
        this.isClosed = isClosed;
        this.customer = customer;
    }

    public ExerciseState create(final String startDate, final String endDate, final CustomerState customer) {
        return new ExerciseState(this.id, startDate, endDate, this.closetAt, this.isClosed, customer);
    }

    public ExerciseState close(final String closetAt, final boolean isClosed) {
        return new ExerciseState(this.id, this.startDate, this.endDate, closetAt, isClosed, this.customer);
    }

    public ExerciseState changeEndDate(final String endDate) {
        return new ExerciseState(this.id, this.startDate, endDate, this.closetAt, this.isClosed, this.customer);
    }

    public ExerciseState changeStartDate(final String startDate) {
        return new ExerciseState(this.id, startDate, this.endDate, this.closetAt, this.isClosed, this.customer);
    }

}
