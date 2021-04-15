package com.hamzajg.accounting.rental.model.rentalcontract;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import com.hamzajg.accounting.rental.model.*;

public interface RentalContract {

  Completes<RentalContractState> create(final String startDate, final String endDate, final String customerId, final int paymentPeriod, final Money price);

  static Completes<RentalContractState> create(final Stage stage, final String startDate, final String endDate, final String customerId, final int paymentPeriod, final Money price) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final RentalContract _rentalContract = stage.actorFor(RentalContract.class, Definition.has(RentalContractEntity.class, Definition.parameters(_address.idString())), _address);
    return _rentalContract.create(startDate, endDate, customerId, paymentPeriod, price);
  }

  Completes<RentalContractState> terminate(final String terminationDate);

}