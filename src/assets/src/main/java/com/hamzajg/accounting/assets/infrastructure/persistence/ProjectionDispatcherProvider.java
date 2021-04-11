package com.hamzajg.accounting.assets.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.xoom.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

import com.hamzajg.accounting.assets.model.journal.JournalLineAdded;
import com.hamzajg.accounting.assets.model.journal.JournalTypeChanged;
import com.hamzajg.accounting.assets.model.journal.JournalLineRemoved;
import com.hamzajg.accounting.assets.model.journal.JournalTitleChanged;
import com.hamzajg.accounting.assets.model.journal.JournalLineChanged;
import com.hamzajg.accounting.assets.model.journal.JournalDateChanged;
import com.hamzajg.accounting.assets.model.journal.JournalCreated;

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
                    ProjectToDescription.with(JournalProjectionActor.class, JournalLineRemoved.class.getName(), JournalTypeChanged.class.getName(), JournalLineChanged.class.getName(), JournalLineAdded.class.getName(), JournalCreated.class.getName(), JournalDateChanged.class.getName(), JournalTitleChanged.class.getName())
                    );

    final Protocols dispatcherProtocols =
            stage.actorFor(
                    new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
                    Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

    final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

    instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

    return instance;
  }

  private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }
}
