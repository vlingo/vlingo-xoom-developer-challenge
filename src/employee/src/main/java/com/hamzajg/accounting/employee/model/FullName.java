package com.hamzajg.accounting.employee.model;

public final class FullName {

  public final String tirsName;
  public final String secondName;
  public final String lastName;

  public static FullName from(final String tirsName, final String secondName, final String lastName) {
    return new FullName(tirsName, secondName, lastName);
  }

  private FullName (final String tirsName, final String secondName, final String lastName) {
    this.tirsName = tirsName;
    this.secondName = secondName;
    this.lastName = lastName;
  }

}