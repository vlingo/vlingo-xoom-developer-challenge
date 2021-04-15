package com.hamzajg.accounting.buying.infrastructure.exchange;

import com.hamzajg.accounting.buying.infrastructure.VendorData;
import com.hamzajg.accounting.buying.model.vendor.Vendor;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

public class VendorExchangeReceivers {

    /**
     * See <a href="https://docs.vlingo.io/vlingo-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
     */
    static class VendorCreated implements ExchangeReceiver<VendorData> {

        private final Grid stage;

        public VendorCreated(final Grid stage) {
            this.stage = stage;
        }

        @Override
        public void receive(final VendorData data) {
            Vendor.create(stage, data.name, data.activityType);
        }
    }

}
