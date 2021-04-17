package com.hamzajg.accounting.assets.model;

public final class JournalLine {

    public final String id;
    public final String clientId;
    public final String vendorId;
    public final Money credit;
    public final Money debit;
    public final String description;

    public static JournalLine from(final String id, final String clientId, final String vendorId, final Money credit, final Money debit, final String description) {
        return new JournalLine(id, clientId, vendorId, credit, debit, description);
    }

    private JournalLine(final String id, final String clientId, final String vendorId, final Money credit, final Money debit, final String description) {
        this.id = id;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.credit = credit;
        this.debit = debit;
        this.description = description;
    }

}