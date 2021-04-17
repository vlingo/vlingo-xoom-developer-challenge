package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.bankaccount.*;
import com.hamzajg.accounting.bank.infrastructure.*;
import com.hamzajg.accounting.bank.infrastructure.Events;

import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class BankAccountProjectionActor extends StateStoreProjectionActor<BankAccountData> {

  private static final BankAccountData Empty = BankAccountData.empty();

  public BankAccountProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public BankAccountProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected BankAccountData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected BankAccountData merge(final BankAccountData previousData, final int previousVersion, final BankAccountData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    BankAccountData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case BankAccountCreated: {
          final BankAccountCreated typedEvent = typed(event);
          final MoneyData balance = MoneyData.from(typedEvent.balance.amount, typedEvent.balance.currency);
          merged = BankAccountData.from(typedEvent.id, typedEvent.rib, typedEvent.iban, typedEvent.type, typedEvent.bicCode, balance, typedEvent.agency);
          break;
        }

        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return merged;
  }
}
