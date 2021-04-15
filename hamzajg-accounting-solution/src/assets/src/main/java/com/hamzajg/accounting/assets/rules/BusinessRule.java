package com.hamzajg.accounting.assets.rules;

public interface BusinessRule {
    boolean isBroken();

    String message();

}
