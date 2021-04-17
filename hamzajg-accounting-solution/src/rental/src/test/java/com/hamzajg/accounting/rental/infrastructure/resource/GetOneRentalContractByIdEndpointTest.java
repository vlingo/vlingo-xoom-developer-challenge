package com.hamzajg.accounting.rental.infrastructure.resource;

import com.hamzajg.accounting.rental.infrastructure.MoneyData;
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GetOneRentalContractByIdEndpointTest extends ResourceTestCase {
    @Test
    public void canGetOneRentalContractById() {
        var location = givenRentalContractWasCreated(RentalContractData.from(null,
                LocalDate.of(2000, 1, 1).toString(),
                LocalDate.of(2010, 12, 31).toString(), null, 6,
                MoneyData.from(700, "TND")));

        givenJsonClient()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body(
                        "id", notNullValue()
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

    private String locationToId(String location) {
        return location.replaceFirst("/rentals/", "");
    }
}
