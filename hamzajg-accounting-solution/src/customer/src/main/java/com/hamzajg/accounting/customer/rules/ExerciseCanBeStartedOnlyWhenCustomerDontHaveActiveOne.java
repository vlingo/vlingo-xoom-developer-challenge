package com.hamzajg.accounting.customer.rules;

public class ExerciseCanBeStartedOnlyWhenCustomerDontHaveActiveOne implements BusinessRule {

    @Override
    public boolean isBroken() {
        return false;
    }

    public ExerciseCanBeStartedOnlyWhenCustomerDontHaveActiveOne(String customerId) {

    }

    @Override
    public String message() {
        return "Exercise Can Be Started Only When Customer Dont Have Active One.";
    }
}
