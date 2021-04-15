package com.hamzajg.accounting.bank.infrastructure.resource;

import com.hamzajg.accounting.bank.infrastructure.JournalData;
import com.hamzajg.accounting.bank.infrastructure.persistence.JournalQueries;
import com.hamzajg.accounting.bank.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.bank.model.JournalLine;
import com.hamzajg.accounting.bank.model.Money;
import com.hamzajg.accounting.bank.model.journal.Journal;
import com.hamzajg.accounting.bank.model.journal.JournalEntity;
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
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class JournalResource extends DynamicResourceHandler {
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
        final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
        return Journal.create(grid, data.date, data.description, journalLines)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id))), serialized(JournalData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> changeDate(final String id, final JournalData data) {
        return resolve(id)
                .andThenTo(journal -> journal.changeDate(data.date))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeDescription(final String id, final JournalData data) {
        return resolve(id)
                .andThenTo(journal -> journal.changeDescription(data.description))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> addJournalLines(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
        return resolve(id)
                .andThenTo(journal -> journal.addJournalLines(journalLines))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> removeJournalLines(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
        return resolve(id)
                .andThenTo(journal -> journal.removeJournalLines(journalLines))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(JournalData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeJouralLines(final String id, final JournalData data) {
        final Money credit = Money.from(data.journalLines.credit.amount, data.journalLines.credit.currency);
        final Money debit = Money.from(data.journalLines.debit.amount, data.journalLines.debit.currency);
        final JournalLine journalLines = JournalLine.from(data.journalLines.bankAccountId, credit, debit, data.journalLines.clientId);
        return resolve(id)
                .andThenTo(journal -> journal.changeJouralLines(journalLines))
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
                post("/journals/create")
                        .body(JournalData.class)
                        .handle(this::create),
                patch("/journals/{id}/change-date")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeDate),
                patch("/journals/{id}/change-description")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeDescription),
                patch("/journals/{id}/add-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::addJournalLines),
                delete("/journals/{id}/remove-journal-lines")
                        .param(String.class)
                        .param(JournalData.class)
                        .handle(this::removeJournalLines),
                patch("/journals/{id}/change-journal-lines")
                        .param(String.class)
                        .body(JournalData.class)
                        .handle(this::changeJouralLines),
                get("/journals")
                        .handle(this::journals)
        );
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/journals/" + id;
    }

    private Completes<Journal> resolve(final String id) {
        final Address address = grid.addressFactory().from(id);
        return grid.actorOf(Journal.class, address, Definition.has(JournalEntity.class, Definition.parameters(id)));
    }

}
