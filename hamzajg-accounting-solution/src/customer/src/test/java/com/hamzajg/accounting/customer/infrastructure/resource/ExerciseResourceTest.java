package com.hamzajg.accounting.customer.infrastructure.resource;


import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ExerciseResourceTest extends com.hamzajg.accounting.customer.infrastructure.resource.ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/exercises")
                .then()
                .statusCode(200)
                .body(is(equalTo("Customer context, Exercise Resource: V0.0.1")));
    }
}
