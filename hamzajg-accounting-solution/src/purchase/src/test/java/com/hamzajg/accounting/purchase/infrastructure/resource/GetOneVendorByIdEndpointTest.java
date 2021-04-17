package com.hamzajg.accounting.purchase.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;

import org.junit.jupiter.api.Test;

public class GetOneVendorByIdEndpointTest extends ResourceTestCase {
  @Test
    public void canGetOneVendorById() {
        var location = givenVendorWasCreated(VendorData.from(null, "Test", "NS"));

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

    private String givenVendorWasCreated(VendorData vendorData) {
        return givenJsonClient()
                .body(vendorData)
                .when()
                .post("/purchases/vendors/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/purchases/vendors/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
    private String locationToId(String location) {
        return location.replaceFirst("/purchases/vendors/", "");
    }
}
