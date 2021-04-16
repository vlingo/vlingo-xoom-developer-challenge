package com.hamzajg.accounting.employee.infrastructure.resource;
import static org.hamcrest.core.IsNull.notNullValue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;

import com.hamzajg.accounting.employee.infrastructure.MoneyData;
import com.hamzajg.accounting.employee.infrastructure.AddressData;
import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.infrastructure.FullNameData;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GetOneEmployeeByIdEndpointTest extends ResourceTestCase {
  @Test
  @Disabled
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
                    "fullName", equalTo(FullNameData.from("TEST", "", "TEST")),
                    "address", equalTo(AddressData.from("TEST", "")),
                    "workingPeriod", equalTo(6),
                    "cost", equalTo(MoneyData.from(700, "TND"))
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
