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
import io.vlingo.xoom.http.resource.Resource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

/**
 * See <a href=
 * "https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
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
        if (data.journalLines == null)
            return Journal.create(grid, LocalDate.parse(data.date), data.type, data.title, data.exerciseId, null)
                    .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(
                            ResponseHeader.of(Location, location(state.id))).and(of(ContentType, "application/json")),
                            serialized(JournalData.from(state)))))
                    .otherwise(arg -> Response.of(NotFound, location()))
                    .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));

        final Set<JournalLine> journalLines = data.journalLines.stream()
                .map(item -> JournalLine.from(item.id, Money.from(item.credit.amount, item.credit.currency),
                        Money.from(item.debit.amount, item.debit.currency), item.description)).collect(Collectors.toSet());

        return Journal.create(grid, LocalDate.parse(data.date), data.type, data.title, data.exerciseId,
                journalLines)
                .andThenTo(state -> Completes
                        .withSuccess(Response.of(Created, ResponseHeader
                                        .headers(ResponseHeader.of(Location,
                                                location(state.id)))
                                        .and(of(ContentType, "application/json")),
                                serialized(JournalData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> changeTitle(final String id, final JournalData data) {
        return resolve(id).andThenTo(journal -> journal.changeTitle(data.title)).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeDate(final String id, final JournalData data) {
        return resolve(id).andThenTo(journal -> journal.changeDate(LocalDate.parse(data.date))).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeType(final String id, final JournalData data) {
        return resolve(id).andThenTo(journal -> journal.changeType(data.type)).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> addJournalLines(final String id, final JournalData data) {
        final Set<JournalLine> journalLines = data.journalLines.stream()
                .map(item -> JournalLine.from(item.id,
                        Money.from(item.credit.amount, item.credit.currency),
                        Money.from(item.debit.amount, item.debit.currency), item.description))
                .collect(Collectors.toSet());
        return resolve(id).andThenTo(journal -> journal.addJournalLines(journalLines)).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeJournalLine(final String id, final JournalData data) {
        final Set<JournalLine> journalLines = data.journalLines.stream()
                .map(item -> JournalLine.from(item.id,
                        Money.from(item.credit.amount, item.credit.currency),
                        Money.from(item.debit.amount, item.debit.currency), item.description))
                .collect(Collectors.toSet());
        return resolve(id).andThenTo(journal -> journal.changeJournalLine(journalLines)).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> removeJournalLines(final String id, final JournalData data) {
        final Set<JournalLine> journalLines = data.journalLines.stream()
                .map(item -> JournalLine.from(item.id,
                        Money.from(item.credit.amount, item.credit.currency),
                        Money.from(item.debit.amount, item.debit.currency), item.description))
                .collect(Collectors.toSet());
        return resolve(id).andThenTo(journal -> journal.removeJournalLines(journalLines)).andThenTo(
                state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> journalById(String journalId) {
        return $queries.journalOf(journalId)
                .andThenTo(JournalData.empty(), state -> Completes.withSuccess(Response.of(Ok,
                        headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noJournal -> Response.of(NotFound));
    }

    public Completes<Response> journals() {
        return $queries.journals()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok,
                        headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    @Override
    public Resource<?> routes() {
        return resource("JournalResource",
                get("/assets").handle(this::index),
                post("/assets/create")
                        .body(JournalData.class)
                        .handle(this::create),
                get("/assets/all")
                        .handle(this::journals),
                patch("/assets/{id}/change-title")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeTitle),
                patch("/assets/{id}/change-date")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeDate),
                patch("/assets/{id}/change-type")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeType),
                patch("/assets/{id}/add-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::addJournalLines),
                patch("/assets/{id}/change-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeJournalLine),
                delete("/assets/{id}/remove-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::removeJournalLines),
                get("/assets/{id}")
                        .param(String.class)
                        .handle(this::journalById));
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
        return grid.actorOf(Journal.class, address,
                Definition.has(JournalEntity.class, Definition.parameters(id)));
    }

}
