package com.hamzajg.accounting.assets.infrastructure.resource;

import com.hamzajg.accounting.assets.infrastructure.JournalData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateAssetsEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewAssets() {
        givenJsonClient()
                .body(JournalData.from(null, LocalDate.of(2021, 1, 1).toString(), null, null, null, null))
                .when()
                .post("/assets/create")
                .then()
                .statusCode(201)
                .body(
                        "id", notNullValue()
                );
    }

}
