package com.hamzajg.accounting.customer.infrastructure.exchange;

import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExchangeTestCase {
    @Test
    @Disabled
    public void canLoadSettings() {

        ExchangeSettings.load(Settings.properties());

        final ConnectionSettings customersExchangeSettings =
                ExchangeSettings.of("customers-exchange").mapToConnection();

        assertNotNull(ExchangeFactory.fanOutInstance(customersExchangeSettings, "customers-exchange", true));
    }
}
