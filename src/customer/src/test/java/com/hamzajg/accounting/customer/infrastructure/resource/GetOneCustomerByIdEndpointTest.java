package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.AddressData;
import com.hamzajg.accounting.customer.infrastructure.CapitalData;
import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import com.hamzajg.accounting.customer.infrastructure.LegalStatusData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GetOneCustomerByIdEndpointTest extends ResourceTestCase {

    private String givenCustomerWasCreated(CustomerData customerData) {
        return givenJsonClient()
                .body(customerData)
                .when()
                .post("/customers/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/customers/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
    private String locationToId(String location) {
        return location.replaceFirst("/customers/", "");
    }

    @Test
    @Disabled("Need to fix Queries...")
    public void canGetOneCustomerById() {
        var location = givenCustomerWasCreated(CustomerData.from(null, "Test", "SARL",
                LocalDate.of(2000, 1, 1).toString(), CapitalData.from(10000),
                AddressData.from("Test Address 1", "Test Address 2"),
                LegalStatusData.from("test", "test", "test"), null));

        givenJsonClient()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(locationToId(location)),
                        "name", equalTo("Test"),
                        "type", equalTo("SARL")
                );
    }

}
