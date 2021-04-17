package com.hamzajg.accounting.bank.infrastructure.resource;

import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.infrastructure.persistence.BankAccountQueries;
import com.hamzajg.accounting.bank.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.bank.model.Money;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccount;
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
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class BankAccountResource extends DynamicResourceHandler {
    private static final String index = "Bank context, Bank Account Resource: V0.0.1";
    private final Grid grid;
    private final BankAccountQueries $queries;

    public BankAccountResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().bankAccountQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> create(final BankAccountData data) {
        final Money balance = Money.from(data.balance.amount, data.balance.currency);
        return BankAccount.create(grid, data.rib, data.iban, data.type, data.bicCode, balance, data.agency)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id)))
                        .and(headers(of(ContentType, "application/json"))), serialized(BankAccountData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> bankAccounts() {
        return $queries.bankAccounts()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> bankAccountById(String bankAccountId) {
        return $queries.bankAccountOf(bankAccountId)
                .andThenTo(BankAccountData.empty(), state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noAccount -> Response.of(NotFound));
    }

    @Override
    public Resource<?> routes() {
        return resource("BankAccountResource",
                post("/banks/accounts/create")
                        .body(BankAccountData.class)
                        .handle(this::create),
                get("/banks/accounts/all")
                        .handle(this::bankAccounts), get("/banks/accounts").handle(this::index),
                get("/banks/accounts/{id}").param(String.class).handle(this::bankAccountById)
        );
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/banks/accounts/" + id;
    }


}
