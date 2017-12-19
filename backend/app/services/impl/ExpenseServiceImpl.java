package services.impl;

import jooq.jooqobjects.tables.pojos.Expense;
import model.ClientExpense;
import model.CurrencyEnum;
import model.ExpenseWithCurrency;
import org.jooq.DSLContext;
import repositories.ExpenseRepository;
import services.CurrencyConversionService;
import services.ExpenseService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ExpenseServiceImpl implements ExpenseService {

    public static final BigDecimal UK_VAT_VALUE = BigDecimal.valueOf(0.2d);

    private ExpenseRepository expenseRepository;
    private CurrencyConversionService currencyConversionService;


    @Inject
    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              CurrencyConversionService currencyConversionService) {
        this.expenseRepository = expenseRepository;
        this.currencyConversionService = currencyConversionService;
    }


    @Override
    public CompletionStage<List<Expense>> findAllExpenses(DSLContext create) {
        return expenseRepository.findAll(create);
    }


    @Override
    public CompletionStage<Void> saveExpense(DSLContext create, ClientExpense clientExpense) {
        ExpenseWithCurrency expenseWithCurrency = clientExpense.toExpenseWithCurrency();
        if (clientExpense.isInPounds()) {
            return expenseRepository.save(create, addVatToExpense(expenseWithCurrency));
        } else {
            return currencyConversionService.getConversionRate(expenseWithCurrency.currency)
                    .thenApply(conversion -> convertToPounds(expenseWithCurrency, conversion))
                    .thenCompose(expenseInPounds -> expenseRepository.save(create, addVatToExpense(expenseInPounds)));
        }
    }


    /**
     * Adds the UK VAT to the expense amount
     *
     * @param expenseWithCurrency the expense with the currency type
     * @return the expenseWithCurrency with the amount updated to include the UK VAT
     */
    private ExpenseWithCurrency addVatToExpense(ExpenseWithCurrency expenseWithCurrency) {
        BigDecimal vat = UK_VAT_VALUE.multiply(expenseWithCurrency.getAmount());
        BigDecimal amountWithVat = expenseWithCurrency.getAmount().add(vat);
        expenseWithCurrency.setAmount(amountWithVat);
        expenseWithCurrency.setVat(UK_VAT_VALUE);
        return expenseWithCurrency;
    }


    /**
     * Converts an {@link ExpenseWithCurrency} containing an amount in a currency other than GBP into one in GBP
     *
     * @param expenseWithCurrency the expense in other currency than GBP
     * @param conversion          the conversion rate between the expense currency and GBP
     * @return an {@link ExpenseWithCurrency} object with the same data but with the amount and currency converted
     */
    private ExpenseWithCurrency convertToPounds(ExpenseWithCurrency expenseWithCurrency, BigDecimal conversion) {
        if (CurrencyEnum.GBP == expenseWithCurrency.getCurrency()) {
            throw new IllegalArgumentException("Cannot convert from Pounds to Pounds");
        }
        BigDecimal amountInPounds = conversion.multiply(expenseWithCurrency.getAmount());
        expenseWithCurrency.setAmount(amountInPounds);
        expenseWithCurrency.setCurrency(CurrencyEnum.GBP);
        return expenseWithCurrency;
    }
}
