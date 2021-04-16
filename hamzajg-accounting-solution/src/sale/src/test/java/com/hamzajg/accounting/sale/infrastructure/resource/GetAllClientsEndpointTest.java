package com.hamzajg.accounting.sale.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.sale.infrastructure.ClientData;

import org.junit.jupiter.api.Test;

public class GetAllClientsEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllClients() {
        givenClientWasCreated(ClientData.from(null, "Test", "NS"));

        givenJsonClient()
                .when()
                .get("/sales/clients/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue(),
                        "[0].name", equalTo("Test"),
                        "[0].activityType", equalTo("NS")
                );
    }

    private String givenClientWasCreated(ClientData clientData) {
        return givenJsonClient()
                .body(clientData)
                .when()
                .post("/sales/clients/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/sales/clients/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
}
