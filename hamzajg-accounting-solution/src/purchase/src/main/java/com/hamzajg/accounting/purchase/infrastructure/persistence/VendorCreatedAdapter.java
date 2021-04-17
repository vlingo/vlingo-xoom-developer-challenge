package com.hamzajg.accounting.purchase.infrastructure.persistence;

import com.hamzajg.accounting.purchase.model.vendor.VendorCreated;

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
public final class VendorCreatedAdapter implements EntryAdapter<VendorCreated,TextEntry> {

  @Override
  public VendorCreated fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final VendorCreated source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(VendorCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VendorCreated source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VendorCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VendorCreated source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VendorCreated.class, 1, serialization, version, metadata);
  }
}
