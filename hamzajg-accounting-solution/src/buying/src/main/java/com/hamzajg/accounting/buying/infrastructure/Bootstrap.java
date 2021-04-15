package com.hamzajg.accounting.buying.infrastructure;

import com.hamzajg.accounting.buying.infrastructure.exchange.ExchangeBootstrap;
import com.hamzajg.accounting.buying.infrastructure.persistence.CommandModelJournalStoreProvider;
import com.hamzajg.accounting.buying.infrastructure.persistence.ProjectionDispatcherProvider;
import com.hamzajg.accounting.buying.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;

@Xoom(name = "buying")
@ResourceHandlers(packages = "com.hamzajg.accounting.buying.infrastructure.resource")
public class Bootstrap implements XoomInitializationAware {

    @Override
    public void onInit(final Grid grid) {
        final ExchangeInitializer exchangeInitializer = new ExchangeBootstrap();
        exchangeInitializer.init(grid);

        final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(grid.world());
        final StatefulTypeRegistry registry = new StatefulTypeRegistry(grid.world());
        QueryModelStateStoreProvider.using(grid.world().stage(), registry);
        CommandModelJournalStoreProvider.using(grid.world().stage(), sourcedTypeRegistry,
                ProjectionDispatcherProvider.using(grid.world().stage()).storeDispatcher,
                exchangeInitializer.dispatcher());
    }

}
