package model;

import jooq.jooqobjects.tables.pojos.Expense;


/**
 * Represents an Expense that specifies the currency of its amount
 */
public class ExpenseWithCurrency extends Expense {

    public CurrencyEnum currency;


    public CurrencyEnum getCurrency() {
        return currency;
    }


    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

}
