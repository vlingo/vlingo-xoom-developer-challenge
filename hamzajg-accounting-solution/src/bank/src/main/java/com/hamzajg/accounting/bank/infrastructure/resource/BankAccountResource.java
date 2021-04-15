package com.hamzajg.accounting.bank.infrastructure.resource;

import com.hamzajg.accounting.bank.infrastructure.persistence.BankAccountQueries;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import com.hamzajg.accounting.bank.infrastructure.BankAccountData;
import com.hamzajg.accounting.bank.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccount;
import com.hamzajg.accounting.bank.model.*;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class BankAccountResource extends DynamicResourceHandler {
  private final Grid grid;
  private final BankAccountQueries $queries;

  public BankAccountResource(final Grid grid) {
      super(grid.world().stage());
      this.grid = grid;
      this.$queries = QueryModelStateStoreProvider.instance().bankAccountQueries;
  }

  public Completes<Response> create(final BankAccountData data) {
    final Money balance = Money.from(data.balance.amount, data.balance.currency);
    return BankAccount.create(grid, data.rib, data.iban, data.type, data.bicCode, balance, data.agency)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(state.id))), serialized(BankAccountData.from(state))))
      .otherwise(arg -> Response.of(NotFound, location()))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> bankAccounts() {
    return $queries.bankAccounts()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("BankAccountResource",
             post("/accounts/create")
            .body(BankAccountData.class)
            .handle(this::create),
             get("/accounts")
            .handle(this::bankAccounts)
     );
  }

  private String location() {
    return location("");
  }

  private String location(final String id) {
    return "/accounts/" + id;
  }


}
