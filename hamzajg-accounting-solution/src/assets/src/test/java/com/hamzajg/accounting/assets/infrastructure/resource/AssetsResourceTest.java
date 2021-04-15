package com.hamzajg.accounting.assets.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AssetsResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/assets")
                .then()
                .statusCode(200)
                .body(is(equalTo("Assets context, Journal Resource: V0.0.1")));
    }
}
