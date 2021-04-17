package com.hamzajg.accounting.employee.infrastructure.resource;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class EmployeeResourceTest extends ResourceTestCase {
    @Test
    public void shouldReturnContextResourceNameAndVersion() {
        givenJsonClient()
                .when()
                .get("/employees")
                .then()
                .statusCode(200)
                .body(is(equalTo("Empolyee context, Empolyee Contract Resource: V0.0.1")));
    }
}
