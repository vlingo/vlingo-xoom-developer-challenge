package com.hamzajg.accounting.assets.infrastructure.resource;

import com.hamzajg.accounting.assets.infrastructure.JournalData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GetAllAssetsEndpointTest extends ResourceTestCase {


    @Test
    @Disabled
    public void canGetAllAssets() {
        givenJournalWasCreated(JournalData.from(null, LocalDate.of(2021, 1, 1).toString(),
                null, null, null, null));

        givenJsonClient()
                .when()
                .get("/assets/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue()
                );
    }

    private void givenJournalWasCreated(JournalData journalData) {
        givenJsonClient()
                .body(journalData)
                .when()
                .post("/assets/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/assets/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
}
