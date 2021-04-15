package com.hamzajg.accounting.rental.infrastructure;

import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "rental")
@ResourceHandlers(packages = "com.hamzajg.accounting.rental.infrastructure.resource")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

}
