package com.hamzajg.accounting.bank.model.bankaccount;

import io.vlingo.xoom.actors.Definition;
import com.hamzajg.accounting.bank.model.*;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface BankAccount {

  Completes<BankAccountState> create(final String rib, final String iban, final String type, final String bicCode, final Money balance, final String agency);

  static Completes<BankAccountState> create(final Stage stage, final String rib, final String iban, final String type, final String bicCode, final Money balance, final String agency) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final BankAccount _bankAccount = stage.actorFor(BankAccount.class, Definition.has(BankAccountEntity.class, Definition.parameters(_address.idString())), _address);
    return _bankAccount.create(rib, iban, type, bicCode, balance, agency);
  }

}