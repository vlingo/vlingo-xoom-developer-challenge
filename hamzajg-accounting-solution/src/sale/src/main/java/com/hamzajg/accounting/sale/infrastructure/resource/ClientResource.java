package com.hamzajg.accounting.sale.infrastructure.resource;

import com.hamzajg.accounting.sale.infrastructure.ClientData;
import com.hamzajg.accounting.sale.infrastructure.persistence.ClientQueries;
import com.hamzajg.accounting.sale.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.sale.model.client.Client;
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
public class ClientResource extends DynamicResourceHandler {
    private static final String index = "Sale context, Client Resource: V0.0.1";

    private final Grid grid;
    private final ClientQueries $queries;

    public ClientResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().clientQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> create(final ClientData data) {
        return Client.create(grid, data.name, data.activityType).andThenTo(state -> Completes
                .withSuccess(
                        Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id)))
                                        .and(headers(of(ContentType, "application/json"))), serialized(ClientData.from(state))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> clients() {
        return $queries.clients().andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> clientById(String clientId) {
        return $queries.clientOf(clientId)
                .andThenTo(ClientData.empty(), state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noClient -> Response.of(NotFound));
    }
    
    @Override
    public Resource<?> routes() {
        return resource("ClientResource", post("/sales/clients/create").body(ClientData.class).handle(this::create),
                get("/sales/clients/all").handle(this::clients), get("/sales/clients").handle(this::index),
                get("/sales/clients/{id}").param(String.class).handle(this::clientById));
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/sales/clients/" + id;
    }

}
