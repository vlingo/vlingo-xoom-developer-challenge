package com.hamzajg.accounting.employee.infrastructure.resource;

import com.hamzajg.accounting.employee.infrastructure.AddressData;
import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.infrastructure.FullNameData;
import com.hamzajg.accounting.employee.infrastructure.MoneyData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

public class GetOneEmployeeByIdEndpointTest extends ResourceTestCase {
    @Test
    public void canGetOneEmployeeById() {
        var location = givenEmployeeWasCreated(EmployeeData.from(null, null,
                FullNameData.from("TEST", "", "TEST"), AddressData.from("TEST", ""),
                6, MoneyData.from(700, "TND")));

        givenJsonClient()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body(
                        "id", notNullValue(),
                        "fullName", notNullValue(),
                        "address", notNullValue(),
                        "workingPeriod", notNullValue(),
                        "cost", notNullValue()
                );
    }

    private String givenEmployeeWasCreated(EmployeeData employeeData) {
        return givenJsonClient()
                .body(employeeData)
                .when()
                .post("/employees/create")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("/employees/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
                .extract()
                .header("Location");
    }

    private String locationToId(String location) {
        return location.replaceFirst("/employees/", "");
    }
}
