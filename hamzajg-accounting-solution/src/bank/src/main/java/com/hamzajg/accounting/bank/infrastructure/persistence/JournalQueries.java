package com.hamzajg.accounting.bank.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.bank.infrastructure.JournalData;

public interface JournalQueries {
  Completes<JournalData> journalOf(String id);
  Completes<Collection<JournalData>> journals();
}