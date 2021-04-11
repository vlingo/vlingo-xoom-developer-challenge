package com.hamzajg.accounting.employee.infrastructure;

import com.hamzajg.accounting.employee.model.FullName;

public class FullNameData {

  public final String firstName;
  public final String secondName;
  public final String lastName;

  public static FullNameData from(final FullName fullName) {
    return from(fullName.tirsName, fullName.secondName, fullName.lastName);
  }

  public static FullNameData from(final String firstName, final String secondName, final String lastName) {
    return new FullNameData(firstName, secondName, lastName);
  }

  private FullNameData (final String firstName, final String secondName, final String lastName) {
    this.firstName = firstName;
    this.secondName = secondName;
    this.lastName = lastName;
  }

}