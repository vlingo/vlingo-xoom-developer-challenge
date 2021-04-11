package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.AssociateData;
import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import com.hamzajg.accounting.customer.infrastructure.persistence.CustomerQueries;
import com.hamzajg.accounting.customer.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import com.hamzajg.accounting.customer.model.customer.Customer;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class CustomerResource extends DynamicResourceHandler {
    private static final String index = "Customer context, Customer Resource: V0.0.1";

    private final CustomerQueries $queries;

    public CustomerResource(final Stage stage) {
        super(stage);
        this.$queries = QueryModelStateStoreProvider.instance().customerQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> create(CustomerData data) {
        return Customer.create(stage(), data.name, data.type, data.activityType, LocalDate.parse(data.creationDate), Capital.from(data.capital.value),
                Address.from(data.address.firstLine, data.address.secondLine),
                LegalStatus.from(data.legalStatus.fiscalCode, data.legalStatus.patent, data.legalStatus.commercialRegistry))
                .andThenTo(state -> Completes.withSuccess(Response.of(Created,
                        headers(of(Location, customerLocation(state.id))).and(of(ContentType, "application/json")), serialized(CustomerData.from(state)))));
    }

    public Completes<Response> addAssociates(String customerId, AssociateData[] data) {
        return resolve(customerId)
                .andThenTo(customer -> customer.addAssociates(Arrays.stream(data).map(item -> Associate.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet())))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(CustomerData.from(state)))))
                .otherwise(noCustomer -> Response.of(NotFound));
    }

    public Completes<Response> customerById(String customerId) {
        return $queries.customerOf(customerId)
                .andThenTo(CustomerData.empty(), state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noCustomer -> Response.of(NotFound));
    }

    public Completes<Response> customers() {
        return $queries.customers()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    private String location() {
        return customerLocation("");
    }

    private Completes<Customer> resolve(final String customerId) {
        return stage().actorOf(Customer.class, stage().addressFactory().from(customerId));
    }

    private String customerLocation(final String customerId) {
        return "/customers/" + customerId;
    }

    public Resource<?> routes() {
        return resource("Customer Resource", get("/customers").handle(this::index),
                get("/customers/all").handle(this::customers),
                get("/customers/{id}").param(String.class).handle(this::customerById),
                post("/customers/create").body(CustomerData.class).handle(this::create),
                patch("/customers/{customerId}/associates/add").param(String.class).body(AssociateData[].class).handle(this::addAssociates)
        );
    }
}
