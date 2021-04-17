package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GetAllExercisesEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllExercises() {
        String id = locationToId(
                givenCustomerWasCreated(CustomerData.from(null, "Test", "SARL", "NS",
                        LocalDate.of(2000, 1, 1).toString(), CapitalData.from(10000),
                        AddressData.from("Test Address 1", "Test Address 2"),
                        LegalStatusData.from("test", "test", "test"), null))
        );
        givenExerciseWasCreated(ExerciseData.from(UUID.randomUUID().toString(), LocalDate.of(2021, 1, 1).toString(),
                LocalDate.of(2021, 12, 31).toString(), id));

        givenJsonClient()
                .when()
                .get("/exercises/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue(),
                        "[0].startDate", equalTo("2021-01-01"),
                        "[0].endDate", equalTo("2021-12-31")
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
