package com.hamzajg.accounting.customer.model.customer;

import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

import java.time.LocalDate;
import java.util.Set;

public interface Customer {

    Completes<CustomerState> create(final String name, final String type, final LocalDate creationDate, final Capital capital, final Address address, final LegalStatus legalStatus);

    static Completes<CustomerState> create(final Stage stage, final String name, final String type, final LocalDate creationDate, final Capital capital, final Address address, final LegalStatus legalStatus) {
        final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
        final Customer _customer = stage.actorFor(Customer.class, Definition.has(CustomerEntity.class, Definition.parameters(_address.idString())), _address);
        return _customer.create(name, type, creationDate, capital, address, legalStatus);
    }

    Completes<CustomerState> addAssociates(final Set<Associate> associates);

    Completes<CustomerState> removeAssociates(final Set<Associate> associates);

}