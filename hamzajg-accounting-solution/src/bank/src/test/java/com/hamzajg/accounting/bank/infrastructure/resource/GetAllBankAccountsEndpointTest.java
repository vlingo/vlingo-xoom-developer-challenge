package com.hamzajg.accounting.bank.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.infrastructure.MoneyData;

import org.junit.jupiter.api.Test;

public class GetAllBankAccountsEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllBankAccounts() {
        givenBankAccountWasCreated(BankAccountData.from(null, "", "","","", MoneyData.from(700, "TND"), ""));

        givenJsonClient()
                .when()
                .get("/banks/accounts/all")
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue()
                );
    }

    private String givenBankAccountWasCreated(BankAccountData bankAccountData) {
        return givenJsonClient()
                .body(bankAccountData)
                .when()
                .post("/banks/accounts/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/banks/accounts/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }
}
