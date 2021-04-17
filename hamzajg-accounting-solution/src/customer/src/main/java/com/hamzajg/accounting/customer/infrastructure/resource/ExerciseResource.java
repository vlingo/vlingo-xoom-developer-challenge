package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import com.hamzajg.accounting.customer.infrastructure.persistence.ExerciseQueries;
import com.hamzajg.accounting.customer.infrastructure.persistence.QueryModelStateStoreProvider;
import com.hamzajg.accounting.customer.model.customer.Customer;
import com.hamzajg.accounting.customer.model.exercise.Exercise;
import com.hamzajg.accounting.customer.model.exercise.ExerciseEntity;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

import java.time.LocalDate;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class ExerciseResource extends DynamicResourceHandler {
    private static final String index = "Customer context, Exercise Resource: V0.0.1";
    private final ExerciseQueries $queries;

    public ExerciseResource(final Stage stage) {
        super(stage);
        this.$queries = QueryModelStateStoreProvider.instance().exerciseQueries;
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> createExercise(ExerciseData data) {
        return find(data.customerId)
                .andThenTo(customer -> Exercise.create(stage(), LocalDate.parse(data.startDate), LocalDate.parse(data.endDate),
                        data.customerId))
                .andThenTo(state -> Completes.withSuccess(Response.of(Created,
                        headers(of(Location, exerciseLocation(state.id))).and(of(ContentType, "application/json")),
                        serialized(ExerciseData.from(state)))))
                .otherwise(noCustomer -> Response.of(NotFound));
    }

    public Completes<Response> exercises() {
        return $queries.exercises()
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
                .otherwise(arg -> Response.of(NotFound, location()))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> close(final String id, final ExerciseData data) {
        return resolve(id)
                .andThenTo(exercise -> exercise.close(LocalDate.parse(data.closedAt), data.isClosed))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(ExerciseData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, exerciseLocation(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeStartDate(final String id, final ExerciseData data) {
        return resolve(id)
                .andThenTo(exercise -> exercise.changeStartDate(LocalDate.parse(data.startDate)))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(ExerciseData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, exerciseLocation(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> changeEndDate(final String id, final ExerciseData data) {
        return resolve(id)
                .andThenTo(exercise -> exercise.changeEndDate(LocalDate.parse(data.endDate)))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(ExerciseData.from(state)))))
                .otherwise(noGreeting -> Response.of(NotFound, exerciseLocation(id)))
                .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
    }

    public Completes<Response> exerciseById(String exerciseId) {
        return $queries.exerciseOf(exerciseId)
                .andThenTo(ExerciseData.empty(), state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(state))))
                .otherwise(noExercise -> Response.of(NotFound));
    }

    private String location() {
        return exerciseLocation("");
    }

    private Completes<Customer> find(final String customerId) {
        return stage().actorOf(Customer.class, stage().addressFactory().from(customerId));
    }

    private Completes<Exercise> resolve(final String id) {
        final Address address = stage().addressFactory().from(id);
        return stage().actorOf(Exercise.class, address, Definition.has(ExerciseEntity.class, Definition.parameters(id)));
    }

    private String exerciseLocation(final String exerciseId) {
        return "/exercises/" + exerciseId;
    }

    public Resource<?> routes() {
        return resource("Exercise Resource", get("/exercises").handle(this::index),
                get("/exercises/all").handle(this::exercises),
                post("/exercises/create").body(ExerciseData.class).handle(this::createExercise),
                get("/exercises/{id}")
                        .param(String.class)
                        .handle(this::exerciseById),
                patch("/exercises/{id}/close")
                        .param(String.class)
                        .body(ExerciseData.class)
                        .handle(this::close),
                patch("/exercises/{id}/change-start-date")
                        .param(String.class)
                        .body(ExerciseData.class)
                        .handle(this::changeStartDate),
                patch("/exercises/{id}/change-end-date")
                        .param(String.class)
                        .body(ExerciseData.class)
                        .handle(this::changeEndDate)
        );
    }
}
