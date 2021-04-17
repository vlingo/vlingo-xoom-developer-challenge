package com.hamzajg.accounting.purchase.infrastructure.resource;

import com.hamzajg.accounting.purchase.infrastructure.VendorData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateVendorEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewVendor() {
        givenJsonClient()
                .body(VendorData.from(null, "Test", "NS"))
                .when()
                .post("purchases/vendors/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/purchases/vendors/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
