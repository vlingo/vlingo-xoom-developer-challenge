package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.XoomInitializer;
import com.hamzajg.accounting.customer.infrastructure.exchange.ExchangeBootstrap;
import com.hamzajg.accounting.customer.infrastructure.persistence.CommandModelStateStoreProvider;
import com.hamzajg.accounting.customer.infrastructure.persistence.ProjectionDispatcherProvider;
import com.hamzajg.accounting.customer.infrastructure.persistence.QueryModelStateStoreProvider;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.turbo.Boot;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

abstract class ResourceTestCase {
    private XoomInitializer xoom;

    @BeforeEach
    public void setUp() throws Exception {
        XoomInitializer.main(new String[]{});
        xoom = XoomInitializer.instance();
        Boolean startUpSuccess = xoom.server().startUp().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @AfterEach
    public void cleanUp() {
        xoom.server().stop();

        QueryModelStateStoreProvider.reset();
        ProjectionDispatcherProvider.reset();
        CommandModelStateStoreProvider.reset();
    }

    protected RequestSpecification givenJsonClient() {
        return given()
                .port(18080)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
