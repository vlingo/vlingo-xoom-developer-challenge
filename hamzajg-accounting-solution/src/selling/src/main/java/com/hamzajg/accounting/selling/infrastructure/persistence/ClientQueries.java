package com.hamzajg.accounting.selling.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.selling.infrastructure.ClientData;

public interface ClientQueries {
  Completes<ClientData> clientOf(String id);
  Completes<Collection<ClientData>> clients();
}