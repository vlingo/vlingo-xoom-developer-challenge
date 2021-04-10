package com.hamzajg.accounting.customer.infrastructure.resource;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;
import com.hamzajg.accounting.customer.model.customer.Customer;
import com.hamzajg.accounting.customer.model.exercise.Exercise;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class ExerciseResource extends DynamicResourceHandler {
    private static final String index = "Customer context, Exercise Resource: V0.0.1";

    public ExerciseResource(final Stage stage) {
        super(stage);
    }

    public Completes<Response> index() {
        return Completes.withSuccess(Response.of(Ok, index));
    }

    public Completes<Response> createExercise(ExerciseData data) {
        return resolve(data.customer.id)
                .andThenTo(customer -> Exercise.create(stage(), data.startDate, data.endDate, data.customer.id))
                .andThenTo(state -> Completes.withSuccess(Response.of(Created,
                        headers(of(Location, exerciseLocation(state.id))).and(of(ContentType, "application/json")),
                        serialized(ExerciseData.from(state)))))
                .otherwise(noCustomer -> Response.of(NotFound));
    }

    private Completes<Customer> resolve(final String customerId) {
        return stage().actorOf(Customer.class, stage().addressFactory().from(customerId));
    }

    private String exerciseLocation(final String exerciseId) {
        return "/exercises/" + exerciseId;
    }

    public Resource<?> routes() {
        return resource("Exercise Resource", get("/exercises").handle(this::index),
                post("/exercises/create").body(ExerciseData.class).handle(this::createExercise)
        );
    }
}
