package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccount;
import com.hamzajg.accounting.bank.model.*;

public class BankAccountExchangeReceivers {

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class BankAccountCreated implements ExchangeReceiver<BankAccountData> {

    private final Grid stage;

    public BankAccountCreated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final BankAccountData data) {
      final Money balance = Money.from(data.balance.amount, data.balance.currency);
      BankAccount.create(stage, data.rib, data.iban, data.type, data.bicCode, balance, data.agency);
    }
  }

}
