package com.hamzajg.accounting.employee.infrastructure;

import com.hamzajg.accounting.employee.model.Address;

public class AddressData {

  public final String firstLine;
  public final String secondLine;

  public static AddressData from(final Address address) {
    return from(address.firstLine, address.secondLine);
  }

  public static AddressData from(final String firstLine, final String secondLine) {
    return new AddressData(firstLine, secondLine);
  }

  private AddressData (final String firstLine, final String secondLine) {
    this.firstLine = firstLine;
    this.secondLine = secondLine;
  }

}