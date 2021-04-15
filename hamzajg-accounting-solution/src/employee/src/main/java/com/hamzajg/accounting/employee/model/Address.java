package com.hamzajg.accounting.employee.model;

public final class Address {

  public final String firstLine;
  public final String secondLine;

  public static Address from(final String firstLine, final String secondLine) {
    return new Address(firstLine, secondLine);
  }

  private Address (final String firstLine, final String secondLine) {
    this.firstLine = firstLine;
    this.secondLine = secondLine;
  }

}