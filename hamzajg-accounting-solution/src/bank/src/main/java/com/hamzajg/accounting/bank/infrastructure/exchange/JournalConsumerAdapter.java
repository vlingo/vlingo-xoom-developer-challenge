package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

import com.hamzajg.accounting.bank.infrastructure.JournalData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class JournalConsumerAdapter implements ExchangeAdapter<JournalData, String, Message> {

  private final String supportedSchemaName;

  public JournalConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public JournalData fromExchange(final Message exchangeMessage) {
    return new JournalDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final JournalData local) {
    final String messagePayload = new JournalDataMapper().localToExternal(local);
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
