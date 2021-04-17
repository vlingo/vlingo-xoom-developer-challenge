package com.hamzajg.accounting.bank.model.bankaccount;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import com.hamzajg.accounting.bank.model.*;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class BankAccountCreated extends IdentifiedDomainEvent {

  public final String id;
  public final String rib;
  public final String iban;
  public final String type;
  public final String bicCode;
  public final Money balance;
  public final String agency;

  public BankAccountCreated(final BankAccountState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.rib = state.rib;
    this.iban = state.iban;
    this.type = state.type;
    this.bicCode = state.bicCode;
    this.balance = state.balance;
    this.agency = state.agency;
  }

  @Override
  public String identity() {
    return id;
  }
}
