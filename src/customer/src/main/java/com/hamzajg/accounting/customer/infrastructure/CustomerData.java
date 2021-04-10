package com.hamzajg.accounting.customer.infrastructure;

import com.hamzajg.accounting.customer.model.customer.CustomerState;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerData {
    public final String id;
    public final String name;
    public final AddressData address;
    public final LegalStatusData legalStatus;
    public final Set<AssociateData> associates;
    public final LocalDate creationDate;
    public final String type;
    public final CapitalData capital;

    public static CustomerData from(final CustomerState customerState) {
        final AddressData address = customerState.address != null ? AddressData.from(customerState.address) : null;
        final LegalStatusData legalStatus = customerState.legalStatus != null ? LegalStatusData.from(customerState.legalStatus) : null;
        final Set<AssociateData> associates = customerState.associates != null ? customerState.associates.stream().map(AssociateData::from).collect(Collectors.toSet()) : null;
        final CapitalData capital = customerState.capital != null ? CapitalData.from(customerState.capital) : null;
        return from(customerState.id, customerState.name, customerState.type, customerState.creationDate, capital, address, legalStatus, associates);
    }

    public static CustomerData from(final String id, final String name, final String type, final LocalDate creationDate, final CapitalData capital, final AddressData address, final LegalStatusData legalStatus, final Set<AssociateData> associates) {
        return new CustomerData(id, name, type, creationDate, capital, address, legalStatus, associates);
    }

    public static List<CustomerData> from(final List<CustomerState> states) {
        return states.stream().map(CustomerData::from).collect(Collectors.toList());
    }

    public static CustomerData empty() {
        return from(CustomerState.identifiedBy(""));
    }

    private CustomerData(final String id, final String name, final String type, final LocalDate creationDate, final CapitalData capital, final AddressData address, final LegalStatusData legalStatus, final Set<AssociateData> associates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.legalStatus = legalStatus;
        this.associates = associates;
        this.creationDate = creationDate;
        this.type = type;
        this.capital = capital;
    }

}
