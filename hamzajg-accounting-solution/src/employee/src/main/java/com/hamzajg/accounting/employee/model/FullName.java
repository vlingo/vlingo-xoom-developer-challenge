package com.hamzajg.accounting.employee.model;

public final class FullName {

  public final String firstName;
  public final String secondName;
  public final String lastName;

  public static FullName from(final String firstName, final String secondName, final String lastName) {
    return new FullName(firstName, secondName, lastName);
  }

  private FullName (final String firstName, final String secondName, final String lastName) {
    this.firstName = firstName;
    this.secondName = secondName;
    this.lastName = lastName;
  }

}