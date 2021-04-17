package com.hamzajg.accounting.customer.model;

public final class LegalStatus {

  public final String fiscalCode;
  public final String patent;
  public final String commercialRegistry;

  public static LegalStatus from(final String fiscalCode, final String patent, final String commercialRegistry) {
    return new LegalStatus(fiscalCode, patent, commercialRegistry);
  }

  private LegalStatus (final String fiscalCode, final String patent, final String commercialRegistry) {
    this.fiscalCode = fiscalCode;
    this.patent = patent;
    this.commercialRegistry = commercialRegistry;
  }

}