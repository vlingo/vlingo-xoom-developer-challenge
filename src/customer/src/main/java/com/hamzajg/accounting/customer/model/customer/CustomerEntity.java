package com.hamzajg.accounting.customer.model.customer;

import com.hamzajg.accounting.customer.model.Address;
import com.hamzajg.accounting.customer.model.Associate;
import com.hamzajg.accounting.customer.model.Capital;
import com.hamzajg.accounting.customer.model.LegalStatus;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

import java.time.LocalDate;
import java.util.Set;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class CustomerEntity extends StatefulEntity<CustomerState> implements Customer {
    private CustomerState state;

    public CustomerEntity(final String id) {
        super(id);
        this.state = CustomerState.identifiedBy(id);
    }

    @Override
    public Completes<CustomerState> create(final String name, final String type, final LocalDate creationDate, final Capital capital, final Address address, final LegalStatus legalStatus) {
        final CustomerState stateArg = state.create(name, type, creationDate.toString(), capital, address, legalStatus);
        return apply(stateArg, new CustomerCreated(stateArg), () -> state);
    }

    @Override
    public Completes<CustomerState> addAssociates(final Set<Associate> associates) {
        final CustomerState stateArg = state.addAssociates(associates);
        return apply(stateArg, new AssociatesAdded(stateArg), () -> state);
    }

    @Override
    public Completes<CustomerState> removeAssociates(final Set<Associate> associates) {
        final CustomerState stateArg = state.removeAssociates(associates);
        return apply(stateArg, new AssociatesRemoved(stateArg), () -> state);
    }

    /*
     * Received when my current state has been applied and restored.
     *
     * @param state the CustomerState
     */
    @Override
    protected void state(final CustomerState state) {
        this.state = state;
    }

    /*
     * Received when I must provide my state type.
     *
     * @return {@code Class<CustomerState>}
     */
    @Override
    protected Class<CustomerState> stateType() {
        return CustomerState.class;
    }
}
