package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.journal.JournalLinesRemoved;

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
public final class JournalLinesRemovedAdapter implements EntryAdapter<JournalLinesRemoved,TextEntry> {

  @Override
  public JournalLinesRemoved fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final JournalLinesRemoved source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(JournalLinesRemoved.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final JournalLinesRemoved source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, JournalLinesRemoved.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final JournalLinesRemoved source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, JournalLinesRemoved.class, 1, serialization, version, metadata);
  }
}
