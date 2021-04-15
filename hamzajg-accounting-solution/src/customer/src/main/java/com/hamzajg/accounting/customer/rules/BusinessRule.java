package com.hamzajg.accounting.customer.rules;

public interface BusinessRule {
    boolean isBroken();

    String message();

}
