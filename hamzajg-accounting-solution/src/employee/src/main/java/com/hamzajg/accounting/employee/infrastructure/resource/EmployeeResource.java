package com.hamzajg.accounting.employee.infrastructure.resource;

import com.hamzajg.accounting.employee.infrastructure.EmployeeData;
import com.hamzajg.accounting.employee.infrastructure.persistence.EmployeeQueries;
import com.hamzajg.accounting.employee.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.employee.model.Address;
import com.hamzajg.accounting.employee.model.FullName;
import com.hamzajg.accounting.employee.model.Money;
import com.hamzajg.accounting.employee.model.employee.Employee;
import com.hamzajg.accounting.employee.model.employee.EmployeeEntity;
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
public class EmployeeResource extends DynamicResourceHandler {
    private static final String index = "Employee context, Employee Contract Resource: V0.0.1";

    private final Grid grid;
    private final EmployeeQueries $queries;

    public EmployeeResource(final Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.$queries = QueryModelStateStoreProvider.instance().employeeQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> create(final EmployeeData data) {
        final FullName fullName = FullName.from(data.fullName.firstName, data.fullName.secondName,
                data.fullName.lastName);
        final Address address = Address.from(data.address.firstLine, data.address.secondLine);
        final Money cost = Money.from(data.cost.amount, data.cost.currency);
        return Employee.create(grid, data.exerciseId, fullName, address, data.workingPeriod, cost)
                .andThenTo(state -> Completes
                        .withSuccess(Response.of(Created,
                                ResponseHeader.headers(ResponseHeader.of(Location, location(state.id))),
                                serialized(EmployeeData.from(state))))
                        .otherwise(arg -> Response.of(NotFound, location()))
                        .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
    }

    public Completes<Response> changeWorkingPeriod(final String id, final EmployeeData data) {
        return resolve(id).andThenTo(employee -> employee.changeWorkingPeriod(data.workingPeriod))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(EmployeeData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeCost(final String id, final EmployeeData data) {
        final Money cost = Money.from(data.cost.amount, data.cost.currency);
        return resolve(id).andThenTo(employee -> employee.changeCost(cost))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(EmployeeData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, location(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> employees() {
        return $queries.employees().andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> employeeById(String employeeId) {
        return $queries.employeeOf(employeeId)
                .andThenTo(EmployeeData.empty(), state -> Completes.withSuccess(Response.of(Ok,
                        headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noJournal -> Response.of(NotFound));
    }

    @Override
    public Resource<?> routes() {
        return resource("EmployeeResource", post("/employees/create").body(EmployeeData.class).handle(this::create),
                patch("/employees/{id}/change-working-period").param(String.class).body(EmployeeData.class)
                        .handle(this::changeWorkingPeriod),
                patch("/employees/{id}/change-cost").param(String.class).body(EmployeeData.class)
                        .handle(this::changeCost),
                get("/employees/{id}").param(String.class).handle(this::employeeById),
                get("/employees/all").handle(this::employees), get("/employees").handle(this::index));
    }

    private String location() {
        return location("");
    }

    private String location(final String id) {
        return "/employees/" + id;
    }

    private Completes<Employee> resolve(final String id) {
        final io.vlingo.xoom.actors.Address address = grid.addressFactory().from(id);
        return grid.actorOf(Employee.class, address, Definition.has(EmployeeEntity.class, Definition.parameters(id)));
    }

}
