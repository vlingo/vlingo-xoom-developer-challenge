package com.hamzajg.accounting.assets.infrastructure.resource;

import com.hamzajg.accounting.assets.infrastructure.JournalData;
import com.hamzajg.accounting.assets.infrastructure.persistence.JournalQueries;
import com.hamzajg.accounting.assets.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.assets.model.JournalLine;
import com.hamzajg.accounting.assets.model.Money;
import com.hamzajg.accounting.assets.model.journal.Journal;
import com.hamzajg.accounting.assets.model.journal.JournalEntity;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.ObjectResponse;
import io.vlingo.xoom.http.resource.Resource;

import java.time.LocalDate;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class JournalResource extends DynamicResourceHandler {
    private static final String index = "Assets context, Journal Resource: V0.0.1";

    private final Grid grid;
    private final JournalQueries $queries;

    public JournalResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().journalQueries;
    }

    public Completes<Response> create(final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.id, credit, debit, data.journalLines.description);
        return Journal.create(grid, LocalDate.parse(data.date), data.type, data.title, data.exerciseId, journalLines)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id))), serialized(JournalData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> changeTitle(final String id, final JournalData data) {
        return resolve(id)
                .andThenTo(journal -> journal.changeTitle(data.title))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeDate(final String id, final JournalData data) {
        return resolve(id)
                .andThenTo(journal -> journal.changeDate(LocalDate.parse(data.date)))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeType(final String id, final JournalData data) {
        return resolve(id)
                .andThenTo(journal -> journal.changeType(data.type))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> addJournalLines(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.id, credit, debit, data.journalLines.description);
        return resolve(id)
                .andThenTo(journal -> journal.addJournalLines(journalLines))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeJournalLine(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.id, credit, debit, data.journalLines.description);
        return resolve(id)
                .andThenTo(journal -> journal.changeJournalLine(journalLines))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> removeJournalLines(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.id, credit, debit, data.journalLines.description);
        return resolve(id)
                .andThenTo(journal -> journal.removeJournalLines(journalLines))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> journals() {
        return $queries.journals()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    @Override
    public Resource<?> routes() {
        return resource("JournalResource",
                io.vlingo.xoom.http.resource.ResourceBuilder.get("/assets")
                        .handle(this::index),
                io.vlingo.xoom.http.resource.ResourceBuilder.post("/assets/create")
                        .body(JournalData.class)
                        .handle(this::create),
                io.vlingo.xoom.http.resource.ResourceBuilder.patch("/assets/{id}/change-title")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeTitle),
                io.vlingo.xoom.http.resource.ResourceBuilder.patch("/assets/{id}/change-date")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeDate),
                io.vlingo.xoom.http.resource.ResourceBuilder.patch("/assets/{id}/change-type")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeType),
                io.vlingo.xoom.http.resource.ResourceBuilder.patch("/assets/{id}/add-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::addJournalLines),
                io.vlingo.xoom.http.resource.ResourceBuilder.patch("/assets/{id}/change-journal-line")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeJournalLine),
                io.vlingo.xoom.http.resource.ResourceBuilder.delete("/assets/{id}/remove-journal-line")
                        .param(String.class)
                        .param(JournalData.class)
                        .handle(this::removeJournalLines),
                io.vlingo.xoom.http.resource.ResourceBuilder.get("/assets/all")
                        .handle(this::journals)
        );
    }
    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }


    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/assets/" + id;
    }

    private Completes<Journal> resolve(final String id) {
        final Address address = grid.addressFactory().from(id);
        return grid.actorOf(Journal.class, address, Definition.has(JournalEntity.class, Definition.parameters(id)));
    }

}
