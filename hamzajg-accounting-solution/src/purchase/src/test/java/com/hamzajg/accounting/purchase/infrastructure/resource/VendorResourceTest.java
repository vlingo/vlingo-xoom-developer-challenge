package com.hamzajg.accounting.purchase.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class VendorResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/purchases/vendors")
                .then()
                .statusCode(200)
                .body(is(equalTo("Purchase context, Vendor Resource: V0.0.1")));
    }
}
