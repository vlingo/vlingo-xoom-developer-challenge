package com.hamzajg.accounting.customer.infrastructure;

import com.hamzajg.accounting.customer.model.Associate;

public class AssociateData {

  public final String fullName;
  public final double part;
  public final boolean isManager;

  public static AssociateData from(final Associate associate) {
    return from(associate.fullName, associate.part, associate.isManager);
  }

  public static AssociateData from(final String fullName, final double part, final boolean isManager) {
    return new AssociateData(fullName, part, isManager);
  }

  private AssociateData (final String fullName, final double part, final boolean isManager) {
    this.fullName = fullName;
    this.part = part;
    this.isManager = isManager;
  }

}