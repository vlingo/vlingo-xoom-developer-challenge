package com.hamzajg.accounting.customer.model.exercise;

import io.vlingo.xoom.symbio.store.object.StateObject;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object Storage</a>
 */
public final class ExerciseState extends StateObject {

    public final String id;
    public final String startDate;
    public final String endDate;
    public final String closetAt;
    public final boolean isClosed;
    public final String customerId;

    public static ExerciseState identifiedBy(final String id) {
        return new ExerciseState(id, null, null, null, false, null);
    }

    public ExerciseState(final String id, final String startDate, final String endDate, final String closetAt,
                         final boolean isClosed, final String customerId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closetAt = closetAt;
        this.isClosed = isClosed;
        this.customerId = customerId;
    }

    public ExerciseState create(final String startDate, final String endDate, final String customerId) {
        return new ExerciseState(this.id, startDate, endDate, this.closetAt, this.isClosed, customerId);
    }

    public ExerciseState close(final String closetAt, final boolean isClosed) {
        return new ExerciseState(this.id, this.startDate, this.endDate, closetAt, isClosed, this.customerId);
    }

    public ExerciseState changeEndDate(final String endDate) {
        return new ExerciseState(this.id, this.startDate, endDate, this.closetAt, this.isClosed, this.customerId);
    }

    public ExerciseState changeStartDate(final String startDate) {
        return new ExerciseState(this.id, startDate, this.endDate, this.closetAt, this.isClosed, this.customerId);
    }

}
