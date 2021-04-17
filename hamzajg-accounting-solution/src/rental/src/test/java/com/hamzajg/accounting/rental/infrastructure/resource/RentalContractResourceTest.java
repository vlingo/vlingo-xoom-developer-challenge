package com.hamzajg.accounting.rental.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class RentalContractResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/rentals")
                .then()
                .statusCode(200)
                .body(is(equalTo("Rental context, Rental Contract Resource: V0.0.1")));
    }
}
