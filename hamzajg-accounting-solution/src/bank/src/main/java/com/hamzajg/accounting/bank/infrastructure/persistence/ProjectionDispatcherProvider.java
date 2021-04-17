package com.hamzajg.accounting.bank.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.xoom.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

import com.hamzajg.accounting.bank.model.journal.JournalLinesAdded;
import com.hamzajg.accounting.bank.model.journal.JournalDescriptionChanged;
import com.hamzajg.accounting.bank.model.journal.JournalLinesChanged;
import com.hamzajg.accounting.bank.model.journal.JournalCreated;
import com.hamzajg.accounting.bank.model.journal.JournalLinesRemoved;
import com.hamzajg.accounting.bank.model.bankaccount.BankAccountCreated;
import com.hamzajg.accounting.bank.model.journal.JournalDateChanged;

@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {
  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;
  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider instance() {
    return instance;
  }

  public static ProjectionDispatcherProvider using(final Stage stage) {
    if (instance != null)
      return instance;

    final List<ProjectToDescription> descriptions = Arrays.asList(
        ProjectToDescription.with(BankAccountProjectionActor.class, BankAccountCreated.class.getName()),
        ProjectToDescription.with(JournalProjectionActor.class, JournalLinesRemoved.class.getName(),
            JournalDescriptionChanged.class.getName(), JournalCreated.class.getName(),
            JournalLinesChanged.class.getName(), JournalDateChanged.class.getName(),
            JournalLinesAdded.class.getName()));

    final Protocols dispatcherProtocols = stage.actorFor(
        new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
        Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

    final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

    instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

    return instance;
  }

  private ProjectionDispatcherProvider(final Dispatcher storeDispatcher,
      final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }

  public static void reset() {
    instance = null;
  }
}
