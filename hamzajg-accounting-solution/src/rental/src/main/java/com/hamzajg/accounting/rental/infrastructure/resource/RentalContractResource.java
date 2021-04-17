package com.hamzajg.accounting.rental.infrastructure.resource;

import com.hamzajg.accounting.rental.infrastructure.RentalContractData;
import com.hamzajg.accounting.rental.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.rental.infrastructure.persistence.RentalContractQueries;
import com.hamzajg.accounting.rental.model.Money;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContract;
import com.hamzajg.accounting.rental.model.rentalcontract.RentalContractEntity;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

/**
 * See <a href=
 * "https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class RentalContractResource extends DynamicResourceHandler {
    private static final String index = "Rental context, Rental Contract Resource: V0.0.1";
    private final Grid grid;
    private final RentalContractQueries $queries;

    public RentalContractResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().rentalContractQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> create(final RentalContractData data) {
        final Money price = Money.from(data.price.amount, data.price.currency);
        return RentalContract.create(grid, data.startDate, data.endDate, data.customerId, data.paymentPeriod, price)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id)))
                        .and(of(ContentType, "application/json")), serialized(RentalContractData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> terminate(final String id, final RentalContractData data) {
        return resolve(id).andThenTo(rentalContract -> rentalContract.terminate(data.terminationDate))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")),
                        serialized(RentalContractData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> rentalContracts() {
        return $queries.rentalContracts()
                .andThenTo(data -> Completes
                        .withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> rentalContractById(String rentalContractId) {
        return $queries.rentalContractOf(rentalContractId)
                .andThenTo(RentalContractData.empty(),
                        state -> Completes.withSuccess(
                                Response.of(Ok, headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noContract -> Response.of(NotFound));
    }

    @Override
    public Resource<?> routes() {
        return resource("RentalContractResource",
                get("/rentals").handle(this::index),
                get("/rentals/all").handle(this::rentalContracts),
                post("/rentals/create")
                        .body(RentalContractData.class)
                        .handle(this::create),
                get("/rentals/{id}")
                        .param(String.class)
                        .handle(this::rentalContractById),
                patch("/rentals/{id}/terminate")
                        .param(String.class)
                        .body(RentalContractData.class)
                        .handle(this::terminate));
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/rentals/" + id;
    }

    private Completes<RentalContract> resolve(final String id) {
        final Address address = grid.addressFactory().from(id);
        return grid.actorOf(RentalContract.class, address,
                Definition.has(RentalContractEntity.class, Definition.parameters(id)));
    }

}
