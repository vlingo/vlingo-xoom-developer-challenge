package com.hamzajg.accounting.customer.infrastructure;

import com.hamzajg.accounting.customer.model.LegalStatus;

public class LegalStatusData {

  public final String fiscalCode;
  public final String patent;
  public final String commercialRegistry;

  public static LegalStatusData from(final LegalStatus legalStatus) {
    return from(legalStatus.fiscalCode, legalStatus.patent, legalStatus.commercialRegistry);
  }

  public static LegalStatusData from(final String fiscalCode, final String patent, final String commercialRegistry) {
    return new LegalStatusData(fiscalCode, patent, commercialRegistry);
  }

  private LegalStatusData (final String fiscalCode, final String patent, final String commercialRegistry) {
    this.fiscalCode = fiscalCode;
    this.patent = patent;
    this.commercialRegistry = commercialRegistry;
  }

}