package repositories;

import jooq.jooqobjects.tables.pojos.Expense;
import model.ExpenseWithCurrency;
import org.jooq.DSLContext;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ExpenseRepository {

    /**
     * Retrieves all the available {@link Expense}s
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link Expense}s
     */
    CompletionStage<List<Expense>> findAll(DSLContext create);


    /**
     * Stores a new {@link Expense} in the database
     *
     * @param create              the {@link DSLContext} with which to access and query the database using
     *                            the typesafe SQL Java DSL provided by JOOQ
     * @param expenseWithCurrency the new expense to store
     * @return a future that, if completed successfully, means that the expense was successfully stored
     */
    CompletionStage<Void> save(DSLContext create, ExpenseWithCurrency expenseWithCurrency);

}
