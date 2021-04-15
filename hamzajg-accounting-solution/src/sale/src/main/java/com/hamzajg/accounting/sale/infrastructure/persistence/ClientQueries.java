package com.hamzajg.accounting.sale.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.sale.infrastructure.ClientData;

public interface ClientQueries {
  Completes<ClientData> clientOf(String id);
  Completes<Collection<ClientData>> clients();
}