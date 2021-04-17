package com.hamzajg.accounting.sale.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.sale.infrastructure.ClientData;

import org.junit.jupiter.api.Test;

public class GetOneClientByIdEndpointTest extends ResourceTestCase {
  @Test
    public void canGetOneClientById() {
        var location = givenClientWasCreated(ClientData.from(null, "Test", "NS"));

        givenJsonClient()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(locationToId(location)),
                        "name", equalTo("Test"),
                        "activityType", equalTo("NS")
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
    private String locationToId(String location) {
        return location.replaceFirst("/sales/clients/", "");
    }
}
