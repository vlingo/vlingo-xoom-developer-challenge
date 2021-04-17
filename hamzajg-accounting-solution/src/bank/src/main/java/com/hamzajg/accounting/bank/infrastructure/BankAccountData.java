package com.hamzajg.accounting.bank.infrastructure;

import com.hamzajg.accounting.bank.model.bankaccount.BankAccountState;

import java.util.List;
import java.util.stream.Collectors;

public class BankAccountData {
    public final String id;
    public final String rib;
    public final String iban;
    public final String type;
    public final String bicCode;
    public final MoneyData balance;
    public final String agency;

    public static BankAccountData from(final BankAccountState bankAccountState) {
        final MoneyData balance = bankAccountState.balance != null ? MoneyData.from(bankAccountState.balance) : null;
        return from(bankAccountState.id, bankAccountState.rib, bankAccountState.iban, bankAccountState.type,
                bankAccountState.bicCode, balance, bankAccountState.agency);
    }

    public static BankAccountData from(final String id, final String rib, final String iban, final String type,
                                       final String bicCode, final MoneyData balance, final String agency) {
        return new BankAccountData(id, rib, iban, type, bicCode, balance, agency);
    }

    public static List<BankAccountData> from(final List<BankAccountState> states) {
        return states.stream().map(BankAccountData::from).collect(Collectors.toList());
    }

    public static BankAccountData empty() {
        return from(BankAccountState.identifiedBy(""));
    }

    private BankAccountData(final String id, final String rib, final String iban, final String type, final String bicCode, final MoneyData balance, final String agency) {
        this.id = id;
        this.rib = rib;
        this.iban = iban;
        this.type = type;
        this.bicCode = bicCode;
        this.balance = balance;
        this.agency = agency;
    }

}
