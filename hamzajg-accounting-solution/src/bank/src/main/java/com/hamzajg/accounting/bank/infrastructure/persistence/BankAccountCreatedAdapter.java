package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.bankaccount.BankAccountCreated;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.BaseEntry.TextEntry;
import io.vlingo.xoom.symbio.EntryAdapter;
import io.vlingo.xoom.symbio.Metadata;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#entryadapter-and-entryadapterprovider">
 *   EntryAdapter and EntryAdapterProvider
 * </a>
 */
public final class BankAccountCreatedAdapter implements EntryAdapter<BankAccountCreated,TextEntry> {

  @Override
  public BankAccountCreated fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final BankAccountCreated source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(BankAccountCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final BankAccountCreated source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, BankAccountCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final BankAccountCreated source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, BankAccountCreated.class, 1, serialization, version, metadata);
  }
}
