package com.hamzajg.accounting.bank.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.infrastructure.MoneyData;

public class CreateBankAccountEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewBankAccount() {
        givenJsonClient()
                .body(BankAccountData.from(null, "", "","","", MoneyData.from(700, "TND"), ""))
                .when()
                .post("banks/accounts/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/banks/accounts/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
