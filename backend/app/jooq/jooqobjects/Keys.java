/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects;


import javax.annotation.Generated;

import jooq.jooqobjects.tables.Expense;
import jooq.jooqobjects.tables.records.ExpenseRecord;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>test</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<ExpenseRecord, Integer> IDENTITY_EXPENSE = Identities0.IDENTITY_EXPENSE;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ExpenseRecord> EXPENSE_PKEY = UniqueKeys0.EXPENSE_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<ExpenseRecord, Integer> IDENTITY_EXPENSE = createIdentity(Expense.EXPENSE, Expense.EXPENSE.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<ExpenseRecord> EXPENSE_PKEY = createUniqueKey(Expense.EXPENSE, "expense_pkey", Expense.EXPENSE.ID);
    }
}
