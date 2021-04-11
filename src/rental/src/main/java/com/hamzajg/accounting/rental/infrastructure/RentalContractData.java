package com.hamzajg.accounting.rental.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContractState;

public class RentalContractData {
  public final String id;
  public final String startDate;
  public final String endDate;
  public final String customerId;
  public final int paymentPeriod;
  public final MoneyData price;
  public final boolean isTerminated;
  public final String terminationDate;

  public static RentalContractData from(final RentalContractState rentalContractState) {
    final MoneyData price = rentalContractState.price != null ? MoneyData.from(rentalContractState.price) : null;
    return from(rentalContractState.id, rentalContractState.startDate, rentalContractState.endDate, rentalContractState.customerId, rentalContractState.paymentPeriod, price, rentalContractState.isTerminated, rentalContractState.terminationDate);
  }

  public static RentalContractData from(final String id, final String startDate, final String endDate, final String customerId, final int paymentPeriod, final MoneyData price, final boolean isTerminated, final String terminationDate) {
    return new RentalContractData(id, startDate, endDate, customerId, paymentPeriod, price, isTerminated, terminationDate);
  }

  public static List<RentalContractData> from(final List<RentalContractState> states) {
    return states.stream().map(RentalContractData::from).collect(Collectors.toList());
  }

  public static RentalContractData empty() {
    return from(RentalContractState.identifiedBy(""));
  }

  private RentalContractData (final String id, final String startDate, final String endDate, final String customerId, final int paymentPeriod, final MoneyData price, final boolean isTerminated, final String terminationDate) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.customerId = customerId;
    this.paymentPeriod = paymentPeriod;
    this.price = price;
    this.isTerminated = isTerminated;
    this.terminationDate = terminationDate;
  }

}
