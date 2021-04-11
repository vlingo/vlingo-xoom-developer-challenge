package com.hamzajg.accounting.customer.infrastructure.exchange;

import com.hamzajg.accounting.customer.infrastructure.CustomerData;
import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import com.hamzajg.accounting.customer.model.customer.Customer;
import com.hamzajg.accounting.customer.model.customer.CustomerEntity;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerExchangeReceivers {

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class CustomerCreated implements ExchangeReceiver<CustomerData> {

        private final Grid stage;

        public CustomerCreated(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final CustomerData data) {
            final Address address = Address.from(data.address.firstLine, data.address.secondLine);
            final LegalStatus legalStatus = LegalStatus.from(data.legalStatus.fiscalCode, data.legalStatus.patent, data.legalStatus.commercialRegistry);
            final Capital capital = Capital.from(data.capital.value);
            Customer.create(stage, data.name, data.type, LocalDate.parse(data.creationDate), capital, address, legalStatus);
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class AssociatesAdded implements ExchangeReceiver<CustomerData> {

        private final Grid stage;

        public AssociatesAdded(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final CustomerData data) {
            final Set<Associate> associates = data.associates.stream().map(item -> Associate.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet());
            stage.actorOf(Customer.class, stage.addressFactory().from(data.id), Definition.has(CustomerEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(customer -> customer.addAssociates(associates));
        }
    }

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class AssociatesRemoved implements ExchangeReceiver<CustomerData> {

        private final Grid stage;

        public AssociatesRemoved(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final CustomerData data) {
            final Set<Associate> associates = data.associates.stream().map(item -> Associate.from(item.fullName, item.part, item.isManager)).collect(Collectors.toSet());
            stage.actorOf(Customer.class, stage.addressFactory().from(data.id), Definition.has(CustomerEntity.class, Definition.parameters(data.id)))
                    .andFinallyConsume(customer -> customer.removeAssociates(associates));
        }
    }

}
