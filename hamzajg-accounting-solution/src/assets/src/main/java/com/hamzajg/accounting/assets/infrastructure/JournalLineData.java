package com.hamzajg.accounting.assets.infrastructure;

import com.hamzajg.accounting.assets.model.JournalLine;

public class JournalLineData {

    public final String id;
    public final String clientId;
    public final String vendorId;
    public final MoneyData credit;
    public final MoneyData debit;
    public final String description;

    public static JournalLineData from(final JournalLine journalLine) {
        final MoneyData credit = journalLine.credit != null ? MoneyData.from(journalLine.credit) : null;
        final MoneyData debit = journalLine.debit != null ? MoneyData.from(journalLine.debit) : null;
        return from(journalLine.id, journalLine.clientId, journalLine.vendorId, credit, debit, journalLine.description);
    }

    public static JournalLineData from(final String id, final String clientId, final String vendorId, final MoneyData credit,
                                       final MoneyData debit, final String description) {
        return new JournalLineData(id, clientId, vendorId, credit, debit, description);
    }

    private JournalLineData(final String id, final String clientId, final String vendorId, final MoneyData credit,
                            final MoneyData debit, final String description) {
        this.id = id;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.credit = credit;
        this.debit = debit;
        this.description = description;
    }

}