package com.hamzajg.accounting.employee.infrastructure.resource;

import com.hamzajg.accounting.employee.infrastructure.MoneyData;
import com.hamzajg.accounting.employee.infrastructure.AddressData;
import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.infrastructure.FullNameData;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class CreateEmployeeEndpointTest extends ResourceTestCase {

    @Test
    public void canCreateNewEmployee() {
        givenJsonClient()
                .body(EmployeeData.from(null, null, FullNameData.from("TEST", "", "TEST"),
                AddressData.from("TEST", ""), 6, MoneyData.from(700, "TND")))
                .when()
                .post("employees/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/employees/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

}
