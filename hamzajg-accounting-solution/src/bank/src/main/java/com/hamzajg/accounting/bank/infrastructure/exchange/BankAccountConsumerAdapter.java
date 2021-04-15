package com.hamzajg.accounting.bank.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.MessageParameters;
import io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class BankAccountConsumerAdapter implements ExchangeAdapter<BankAccountData, String, Message> {

  private final String supportedSchemaName;

  public BankAccountConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public BankAccountData fromExchange(final Message exchangeMessage) {
    return new BankAccountDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final BankAccountData local) {
    final String messagePayload = new BankAccountDataMapper().localToExternal(local);
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
