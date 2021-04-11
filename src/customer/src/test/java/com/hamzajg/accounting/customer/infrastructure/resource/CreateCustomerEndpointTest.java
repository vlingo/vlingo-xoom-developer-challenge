package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.AddressData;
import com.hamzajg.accounting.customer.infrastructure.CapitalData;
import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import com.hamzajg.accounting.customer.infrastructure.LegalStatusData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateCustomerEndpointTest extends com.hamzajg.accounting.customer.infrastructure.resource.ResourceTestCase {

    @Test
    public void canCreateNewCustomer() {
        givenJsonClient()
                .body(CustomerData.from(null, "Test", "Test", "NS",
                        LocalDate.of(2000, 1, 1).toString(), CapitalData.from(10000),
                        AddressData.from("Test Address 1", "Test Address 2"),
                        LegalStatusData.from("test", "test", "test"), null))
                .when()
                .post("customers/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/customers/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
