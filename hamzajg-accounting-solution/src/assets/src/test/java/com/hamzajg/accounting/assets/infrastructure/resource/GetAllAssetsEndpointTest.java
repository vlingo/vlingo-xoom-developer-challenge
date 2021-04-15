package com.hamzajg.accounting.assets.infrastructure.resource;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import java.time.LocalDate;

import com.hamzajg.accounting.assets.infrastructure.JournalData;

import org.junit.jupiter.api.Test;

public class GetAllAssetsEndpointTest extends ResourceTestCase {


    @Test
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

    private String givenJournalWasCreated(JournalData journalData) {
        return givenJsonClient()
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
