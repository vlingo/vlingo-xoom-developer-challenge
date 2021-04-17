package com.hamzajg.accounting.assets.infrastructure;


import com.hamzajg.accounting.assets.infrastructure.exchange.ExchangeBootstrap;
import com.hamzajg.accounting.assets.infrastructure.persistence.CommandModelStateStoreProvider;
import com.hamzajg.accounting.assets.infrastructure.persistence.ProjectionDispatcherProvider;
import com.hamzajg.accounting.assets.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;

@Xoom(name = "assets")
@ResourceHandlers(packages = "com.hamzajg.accounting.assets.infrastructure.resource")
public class Bootstrap implements XoomInitializationAware {

    @Override
    public void onInit(final Grid grid) {
        final ExchangeInitializer exchangeInitializer = new ExchangeBootstrap();
        exchangeInitializer.init(grid);

        final StatefulTypeRegistry registry = new StatefulTypeRegistry(grid.world());
        QueryModelStateStoreProvider.using(grid.world().stage(), registry);
        CommandModelStateStoreProvider.using(grid.world().stage(), registry,
                ProjectionDispatcherProvider.using(grid.world().stage()).storeDispatcher,
                exchangeInitializer.dispatcher());
    }

}
