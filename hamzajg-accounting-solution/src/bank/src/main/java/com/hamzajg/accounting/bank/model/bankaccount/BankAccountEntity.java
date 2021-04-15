package com.hamzajg.accounting.bank.model.bankaccount;

import com.hamzajg.accounting.bank.model.*;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class BankAccountEntity extends EventSourced implements BankAccount {
  private BankAccountState state;

  public BankAccountEntity(final String id) {
    super(id);
    this.state = BankAccountState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(BankAccountEntity.class, BankAccountCreated.class, BankAccountEntity::applyBankAccountCreated);
  }

  @Override
  public Completes<BankAccountState> create(final String rib, final String iban, final String type, final String bicCode, final Money balance, final String agency) {
    final BankAccountState stateArg = state.create(rib, iban, type, bicCode, balance, agency);
    return apply(new BankAccountCreated(stateArg), () -> state);
  }

  private void applyBankAccountCreated(final BankAccountCreated event) {
    state = state.create(event.rib, event.iban, event.type, event.bicCode, event.balance, event.agency);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code BankAccountState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <BankAccountState> void restoreSnapshot(final BankAccountState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code BankAccountState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return BankAccountState
   */
  @Override
  protected BankAccountState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
