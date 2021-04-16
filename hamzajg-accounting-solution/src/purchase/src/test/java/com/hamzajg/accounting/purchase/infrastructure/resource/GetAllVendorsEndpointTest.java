package com.hamzajg.accounting.purchase.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;

import org.junit.jupiter.api.Test;

public class GetAllVendorsEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllVendors() {
        givenVendorWasCreated(VendorData.from(null, "Test", "NS"));

        givenJsonClient()
                .when()
                .get("/purchases/vendors/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue(),
                        "[0].name", equalTo("Test"),
                        "[0].activityType", equalTo("NS")
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
}
