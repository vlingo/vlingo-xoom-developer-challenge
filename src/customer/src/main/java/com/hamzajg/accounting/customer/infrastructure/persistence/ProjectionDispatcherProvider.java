package com.hamzajg.accounting.customer.infrastructure.persistence;

import com.hamzajg.accounting.customer.model.customer.AssociatesAdded;
import com.hamzajg.accounting.customer.model.customer.AssociatesRemoved;
import com.hamzajg.accounting.customer.model.customer.CustomerCreated;
import com.hamzajg.accounting.customer.model.exercise.ExerciseClosed;
import com.hamzajg.accounting.customer.model.exercise.ExerciseCreated;
import com.hamzajg.accounting.customer.model.exercise.ExerciseEndDateChanged;
import com.hamzajg.accounting.customer.model.exercise.ExerciseStartDateChanged;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.xoom.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {
    private static ProjectionDispatcherProvider instance;

    public final ProjectionDispatcher projectionDispatcher;
    public final Dispatcher storeDispatcher;

    public static ProjectionDispatcherProvider instance() {
        return instance;
    }

    public static ProjectionDispatcherProvider using(final Stage stage) {
        if (instance != null) return instance;

        final List<ProjectToDescription> descriptions =
                Arrays.asList(
                        ProjectToDescription.with(ExerciseProjectionActor.class, ExerciseCreated.class.getName(), ExerciseStartDateChanged.class.getName(), ExerciseClosed.class.getName(), ExerciseEndDateChanged.class.getName()),
                        ProjectToDescription.with(CustomerProjectionActor.class, AssociatesRemoved.class.getName(), CustomerCreated.class.getName(), AssociatesAdded.class.getName())
                );

        final Protocols dispatcherProtocols =
                stage.actorFor(
                        new Class<?>[]{Dispatcher.class, ProjectionDispatcher.class},
                        Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

        final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

        instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

        return instance;
    }

    private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
        this.storeDispatcher = storeDispatcher;
        this.projectionDispatcher = projectionDispatcher;
    }

    public static void reset() {
        instance = null;
    }
}