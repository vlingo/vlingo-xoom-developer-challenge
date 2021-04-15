package com.hamzajg.accounting.selling.infrastructure.persistence;

import com.hamzajg.accounting.selling.model.client.ClientCreated;

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
public final class ClientCreatedAdapter implements EntryAdapter<ClientCreated,TextEntry> {

  @Override
  public ClientCreated fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final ClientCreated source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(ClientCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ClientCreated source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ClientCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ClientCreated source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ClientCreated.class, 1, serialization, version, metadata);
  }
}
