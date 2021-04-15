package com.hamzajg.accounting.bank.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;

public interface BankAccountQueries {
  Completes<BankAccountData> bankAccountOf(String id);
  Completes<Collection<BankAccountData>> bankAccounts();
}