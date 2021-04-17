package com.hamzajg.accounting.sale.rules;

public interface BusinessRule {
    boolean isBroken();

    String message();

}
