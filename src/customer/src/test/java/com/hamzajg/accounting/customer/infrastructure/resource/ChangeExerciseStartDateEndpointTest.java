package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.*;
import com.hamzajg.accounting.customer.model.customer.CustomerState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class ChangeExerciseStartDateEndpointTest extends ResourceTestCase {

    @Test
    public void canChangeExerciseStartDate() {
        String customerId = locationToId(
                givenCustomerWasCreated(CustomerData.from(null, "Test", "SARL", "NS",
                        LocalDate.of(2000, 1, 1).toString(), CapitalData.from(10000),
                        AddressData.from("Test Address 1", "Test Address 2"),
                        LegalStatusData.from("test", "test", "test"), null))
        );
        String id = locationToId(givenExerciseWasCreated(ExerciseData.from(UUID.randomUUID().toString(), LocalDate.of(2021, 1, 1).toString(),
                LocalDate.of(2021, 12, 31).toString(),
                CustomerData.from(CustomerState.identifiedBy(customerId)))));

        givenJsonClient()
                .body(ExerciseData.from(null, LocalDate.of(2021, 2, 1).toString(), null, null))
                .when()
                .patch(String.format("%s/change-start-date", id))
                .then()
                .statusCode(200)
                .body(
                        "id", notNullValue(),
                        "startDate", equalTo("2021-02-01")
                );
    }

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

    private String givenExerciseWasCreated(ExerciseData exerciseData) {
        return givenJsonClient()
                .body(exerciseData)
                .when()
                .post("exercises/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/exercises/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

    private String locationToId(String location) {
        return location.replaceFirst("/customers/", "");
    }
}
