package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class AddCustomersAssociateEndpointTest extends ResourceTestCase {

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
    public void canAddCustomersAssociates() {
        String id = locationToId(
                givenCustomerWasCreated(CustomerData.from(null, "Test", "Test", "NS", LocalDate.of(2000, 1, 1).toString()
                        , CapitalData.from(10000), AddressData.from("Test Address 1", "Test Address 2"),
                        LegalStatusData.from("test", "test", "test"), null))
        );
        givenJsonClient()
                .body(Arrays.asList(AssociateData.from("Test", 10, true))).when()
                .patch(String.format("/customers/%s/associates/add", id))
                .then()
                .statusCode(200)
                .body(
                        "id", notNullValue(),
                        "name", equalTo("Test"),
                        "type", equalTo("Test"),
                        "creationDate", equalTo(LocalDate.of(2000, 1, 1).toString()),
                        "associates", notNullValue()
                );
    }

}
