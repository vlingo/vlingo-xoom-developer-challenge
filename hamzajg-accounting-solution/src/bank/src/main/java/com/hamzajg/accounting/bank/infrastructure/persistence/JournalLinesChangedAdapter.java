package com.hamzajg.accounting.bank.infrastructure.persistence;

import com.hamzajg.accounting.bank.model.journal.JournalLinesChanged;

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
public final class JournalLinesChangedAdapter implements EntryAdapter<JournalLinesChanged,TextEntry> {

  @Override
  public JournalLinesChanged fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final JournalLinesChanged source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(JournalLinesChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final JournalLinesChanged source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, JournalLinesChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final JournalLinesChanged source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, JournalLinesChanged.class, 1, serialization, version, metadata);
  }
}
