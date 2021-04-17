package com.hamzajg.accounting.rental.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import java.time.LocalDate;

import com.hamzajg.accounting.rental.infrastructure.MoneyData;
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GetAllRentalContractsEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllRentalContracts() {
        givenRentalContractWasCreated(RentalContractData.from(null, LocalDate.of(2000, 1, 1).toString(), 
        LocalDate.of(2010, 12, 31).toString(), null, 6,
                MoneyData.from(700, "TND")));

        givenJsonClient()
                .when()
                .get("/rentals/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue(),
                        "[0].startDate", equalTo("2000-01-01"),
                        "[0].endDate", equalTo("2010-12-31")
                );
    }

    private String givenRentalContractWasCreated(RentalContractData rentalContractData) {
        return givenJsonClient()
                .body(rentalContractData)
                .when()
                .post("/rentals/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/rentals/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
}
