package com.hamzajg.accounting.sale.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ClientResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/sales/clients")
                .then()
                .statusCode(200)
                .body(is(equalTo("Sale context, Client Resource: V0.0.1")));
    }
}
