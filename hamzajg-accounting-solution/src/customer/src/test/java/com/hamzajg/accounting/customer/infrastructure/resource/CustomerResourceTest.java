package com.hamzajg.accounting.customer.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CustomerResourceTest extends com.hamzajg.accounting.customer.infrastructure.resource.ResourceTestCase {
    
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body(is(equalTo("Customer context, Customer Resource: V0.0.1")));
    }
}
