package controllers;

import jooq.jooqobjects.tables.pojos.Expense;
import model.ClientExpense;
import play.libs.Json;
import play.mvc.Result;
import services.ExpenseService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ExpensesController extends BaseController {

    private ExpenseService expenseService;


    /**
     * @param expenseService the service implementing the expenses business logic
     */
    @Inject
    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    /**
     * Retrieves all the available {@link Expense} objects from the database
     *
     * @return Json object representing the available list of expenses
     */
    public CompletionStage<Result> findAllExpenses() {
        return withTransaction(ctx ->
                expenseService.findAllExpenses(ctx)
        ).thenApply(expenses -> ok(Json.toJson(expenses)));
    }


    /**
     * Stores a new expense in the database
     *
     * @return HTTP 200 status code if the operation succedeed, HTTP 400 in case there's a
     * problem with the input or HTTP 500 in case of error
     */
    public CompletionStage<Result> addExpense() {
        ClientExpense clientExpense = Json.fromJson(request().body().asJson(), ClientExpense.class);
        return withTransaction(ctx ->
                expenseService.saveExpense(ctx, clientExpense)
        ).thenApply(nothing -> ok());
    }

}
