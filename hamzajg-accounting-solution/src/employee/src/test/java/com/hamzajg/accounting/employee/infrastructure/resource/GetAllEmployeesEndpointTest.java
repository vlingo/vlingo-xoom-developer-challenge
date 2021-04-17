package com.hamzajg.accounting.employee.infrastructure.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.employee.infrastructure.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GetAllEmployeesEndpointTest extends ResourceTestCase {

    @Test
    public void canGetAllEmployees() {
        givenEmployeeWasCreated(EmployeeData.from(null, null, FullNameData.from("TEST", "", "TEST"),
                AddressData.from("TEST", ""), 6, MoneyData.from(700, "TND")));

        givenJsonClient()
        .when()
        .get("/employees/all")
        .then()
        .statusCode(200)
        .body(
            "[0].id", notNullValue(),
            "[0].fullName", equalTo(FullNameData.from("TEST", "", "TEST")),
            "[0].address", equalTo(AddressData.from("TEST", "")),
            "[0].workingPeriod", equalTo(6),
            "[0].cost", equalTo(MoneyData.from(700, "TND"))
                 );
    }

    private String givenEmployeeWasCreated(EmployeeData employeeData) {
        return givenJsonClient().body(employeeData).when().post("/employees/create").then().statusCode(201)
                .header("Location", matchesRegex("/employees/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")).extract()
                .header("Location");
    }
}
