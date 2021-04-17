package com.hamzajg.accounting.rental.infrastructure.resource;

import com.hamzajg.accounting.rental.infrastructure.MoneyData;
import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import java.time.LocalDate;

public class CreateRentalContractEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewRentalContract() {
        givenJsonClient()
                .body(RentalContractData.from(null, LocalDate.of(2000, 1, 1).toString(), 
                LocalDate.of(2010, 12, 31).toString(), null, 6, MoneyData.from(700, "TND")))
                .when()
                .post("rentals/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/rentals/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
