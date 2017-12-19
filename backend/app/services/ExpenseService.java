package services;

import jooq.jooqobjects.tables.pojos.Expense;
import model.ClientExpense;
import org.jooq.DSLContext;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ExpenseService {

    /**
     * Retrieves all the {@link Expense} objects making use of the {@link DSLContext} provided
     * to access the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of expenses
     */
    CompletionStage<List<Expense>> findAllExpenses(DSLContext create);


    /**
     * Stores an expense in the database applying the necessary currency conversions in case of dealing
     * with currencies othen than GBP
     *
     * @param create        the {@link DSLContext} with which to access and query the database using
     *                      the typesafe SQL Java DSL provided by JOOQ
     * @param clientExpense the expense coming from the client
     * @return a Future that, when redeemed, will have stored the expense in the underlying database
     */
    CompletionStage<Void> saveExpense(DSLContext create, ClientExpense clientExpense);

}
