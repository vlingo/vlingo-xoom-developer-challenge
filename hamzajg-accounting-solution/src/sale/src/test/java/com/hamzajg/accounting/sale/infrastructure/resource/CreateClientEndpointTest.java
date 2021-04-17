package com.hamzajg.accounting.sale.infrastructure.resource;

import com.hamzajg.accounting.sale.infrastructure.ClientData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateClientEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewClient() {
        givenJsonClient()
                .body(ClientData.from(null, "Test", "NS"))
                .when()
                .post("sales/clients/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/sales/clients/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
