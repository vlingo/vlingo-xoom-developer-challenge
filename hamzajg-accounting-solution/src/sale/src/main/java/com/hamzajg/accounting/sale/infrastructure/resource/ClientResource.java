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
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class ClientResource extends DynamicResourceHandler {
    private final Grid grid;
    private final ClientQueries $queries;

    public ClientResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().clientQueries;
    }

    public Completes<Response> create(final ClientData data) {
        return Client.create(grid, data.name, data.activityType)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id))), serialized(ClientData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> clients() {
        return $queries.clients()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    @Override
    public Resource<?> routes() {
        return resource("ClientResource",
                post("/clients/create")
                        .body(ClientData.class)
                        .handle(this::create),
                get("/clients")
                        .handle(this::clients)
        );
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/clients/" + id;
    }


}
