package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.*;
import com.hamzajg.accounting.customer.model.customer.CustomerState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateExerciseEndpointTest extends com.hamzajg.accounting.customer.infrastructure.resource.ResourceTestCase {

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
    public void canCreateNewExerciseForACustomer() {
        String id = locationToId(
                givenCustomerWasCreated(CustomerData.from(null, "Test", "SARL", "NS",
                        LocalDate.of(2000, 1, 1).toString(), CapitalData.from(10000),
                        AddressData.from("Test Address 1", "Test Address 2"),
                        LegalStatusData.from("test", "test", "test"), null))
        );
        givenJsonClient()
                .body(ExerciseData.from(UUID.randomUUID().toString(), LocalDate.now().toString(), LocalDate.now().toString(), CustomerData.from(CustomerState.identifiedBy(id))))
                .when()
                .post("exercises/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/exercises/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

    @Test
    public void cannotCreateNewExerciseForANotExistingCustomer() {
        givenJsonClient()
                .body(ExerciseData.from(UUID.randomUUID().toString(), LocalDate.now().toString(), LocalDate.now().toString(),
                        CustomerData.from(CustomerState.identifiedBy(UUID.randomUUID().toString()))))
                .when()
                .post("exercises/create")
                .then()
                .statusCode(404);
    }
}
