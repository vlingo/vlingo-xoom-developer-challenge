package com.hamzajg.accounting.rental.infrastructure.persistence;

import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import io.vlingo.xoom.common.Completes;

import java.util.Collection;

public interface RentalContractQueries {
    Completes<RentalContractData> rentalContractOf(String id);

    Completes<Collection<RentalContractData>> rentalContracts();
}