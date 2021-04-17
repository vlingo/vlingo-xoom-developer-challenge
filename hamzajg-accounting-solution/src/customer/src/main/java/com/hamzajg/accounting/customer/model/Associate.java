package com.hamzajg.accounting.customer.model;

public final class Associate {

  public final String fullName;
  public final double part;
  public final boolean isManager;

  public static Associate from(final String fullName, final double part, final boolean isManager) {
    return new Associate(fullName, part, isManager);
  }

  private Associate (final String fullName, final double part, final boolean isManager) {
    this.fullName = fullName;
    this.part = part;
    this.isManager = isManager;
  }

}