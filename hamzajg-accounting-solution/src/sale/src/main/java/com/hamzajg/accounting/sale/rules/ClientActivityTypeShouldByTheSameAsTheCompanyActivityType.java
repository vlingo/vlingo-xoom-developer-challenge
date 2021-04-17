package com.hamzajg.accounting.sale.rules;

public class ClientActivityTypeShouldByTheSameAsTheCompanyActivityType implements BusinessRule {

    @Override
    public boolean isBroken() {
        return false;
    }

    public ClientActivityTypeShouldByTheSameAsTheCompanyActivityType(String companyId) {

    }

    @Override
    public String message() {
        return "Client Activity Type Should By The Same As The Company Activity Type.";
    }
}
