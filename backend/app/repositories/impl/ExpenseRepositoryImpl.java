package repositories.impl;

import jooq.jooqobjects.tables.pojos.Expense;
import model.ExpenseWithCurrency;
import org.jooq.DSLContext;
import repositories.ExpenseRepository;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static jooq.jooqobjects.Tables.EXPENSE;

public class ExpenseRepositoryImpl implements ExpenseRepository {

    @Override
    public CompletionStage<List<Expense>> findAll(DSLContext create) {
        return create
                .selectFrom(EXPENSE)
                .fetchAsync()
                .thenApply(record -> record.into(Expense.class));
    }


    @Override
    public CompletionStage<Void> save(DSLContext create, ExpenseWithCurrency expenseWithCurrency) {
        return create
                .insertInto(EXPENSE)
                .columns(EXPENSE.DATE, EXPENSE.AMOUNT, EXPENSE.REASON, EXPENSE.VAT)
                .values(expenseWithCurrency.getDate(), expenseWithCurrency.getAmount(),
                        expenseWithCurrency.getReason(), expenseWithCurrency.getVat())
                .executeAsync()
                .thenRun(() -> {
                });
    }

}
