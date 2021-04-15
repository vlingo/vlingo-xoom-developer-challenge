package com.hamzajg.accounting.rental.infrastructure.exchange;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import com.hamzajg.accounting.rental.model.rentalcontract.RentalContract;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContractEntity;
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import com.hamzajg.accounting.rental.model.*;

public class RentalContractExchangeReceivers {

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class RentalContractCreated implements ExchangeReceiver<RentalContractData> {

    private final Grid stage;

    public RentalContractCreated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final RentalContractData data) {
      final Money price = Money.from(data.price.amount, data.price.currency);
      RentalContract.create(stage, data.startDate, data.endDate, data.customerId, data.paymentPeriod, price);
    }
  }

  /**
   * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class RentalContractTerminated implements ExchangeReceiver<RentalContractData> {

    private final Grid stage;

    public RentalContractTerminated(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final RentalContractData data) {
      stage.actorOf(RentalContract.class, stage.addressFactory().from(data.id), Definition.has(RentalContractEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(rentalContract -> rentalContract.terminate(data.terminationDate));
    }
  }

}
