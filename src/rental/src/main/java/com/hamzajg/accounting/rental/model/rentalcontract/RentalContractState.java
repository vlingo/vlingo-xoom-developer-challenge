package com.hamzajg.accounting.rental.model.rentalcontract;

import com.hamzajg.accounting.rental.model.*;

import io.vlingo.xoom.symbio.store.object.StateObject;

/**
 * See <a href="https://docs.vlingo.io/vlingo-symbio/object-storage">Object Storage</a>
 */
public final class RentalContractState extends StateObject {

  public final String id;
  public final String startDate;
  public final String endDate;
  public final String customerId;
  public final int paymentPeriod;
  public final Money price;
  public final boolean isTerminated;
  public final String terminationDate;

  public static RentalContractState identifiedBy(final String id) {
    return new RentalContractState(id, null, null, null, 0, null, false, null);
  }

  public RentalContractState (final String id, final String startDate, final String endDate, final String customerId, final int paymentPeriod, final Money price, final boolean isTerminated, final String terminationDate) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.customerId = customerId;
    this.paymentPeriod = paymentPeriod;
    this.price = price;
    this.isTerminated = isTerminated;
    this.terminationDate = terminationDate;
  }

  public RentalContractState create(final String startDate, final String endDate, final String customerId, final int paymentPeriod, final Money price) {
    return new RentalContractState(this.id, startDate, endDate, customerId, paymentPeriod, price, this.isTerminated, this.terminationDate);
  }

  public RentalContractState terminate(final String terminationDate) {
    return new RentalContractState(this.id, this.startDate, this.endDate, this.customerId, this.paymentPeriod, this.price, this.isTerminated, terminationDate);
  }

}
