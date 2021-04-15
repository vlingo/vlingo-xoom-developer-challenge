package com.hamzajg.accounting.selling.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import com.hamzajg.accounting.selling.model.client.Client;
import com.hamzajg.accounting.selling.infrastructure.ClientData;

public class ClientExchangeReceivers {

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class ClientCreated implements ExchangeReceiver<ClientData> {

    private final Grid stage;

    public ClientCreated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final ClientData data) {
      Client.create(stage, data.name, data.activityType);
    }
  }

}
