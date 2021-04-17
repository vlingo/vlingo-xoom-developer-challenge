package com.hamzajg.accounting.bank.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class BankAccountResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/banks/accounts")
                .then()
                .statusCode(200)
                .body(is(equalTo("Bank context, Bank Account Resource: V0.0.1")));
    }
}
