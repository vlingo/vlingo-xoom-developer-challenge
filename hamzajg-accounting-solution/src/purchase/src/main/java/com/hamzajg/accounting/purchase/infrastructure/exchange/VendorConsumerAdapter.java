package com.hamzajg.accounting.purchase.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class VendorConsumerAdapter implements ExchangeAdapter<VendorData, String, Message> {

  private final String supportedSchemaName;

  public VendorConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public VendorData fromExchange(final Message exchangeMessage) {
    return new VendorDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final VendorData local) {
    final String messagePayload = new VendorDataMapper().localToExternal(local);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return supportedSchemaName.equalsIgnoreCase(schemaName);
  }

}
