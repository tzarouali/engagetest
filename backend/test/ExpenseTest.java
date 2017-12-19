import com.google.common.collect.ImmutableMap;
import jooq.jooqobjects.tables.pojos.Expense;
import model.ClientExpense;
import model.CurrencyEnum;
import model.ExpenseWithCurrency;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.util.h2.H2DataType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import repositories.impl.ExpenseRepositoryImpl;
import services.CurrencyConversionService;
import services.ExpenseService;
import services.impl.ExpenseServiceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class ExpenseTest {

    private Database database;


    @Before
    public void setupDatabase() {
        database = Databases.createFrom(
                "backend_db",
                "org.h2.Driver",
                "jdbc:h2:mem:backend_db;INIT=CREATE SCHEMA IF NOT EXISTS TEST\\;SET SCHEMA TEST;",
                ImmutableMap.of(
                        "user", "backend_user",
                        "password", "backend_user"
                )
        );
        Connection connection = database.getConnection();

        DSLContext dslContext = DSL.using(
                connection,
                SQLDialect.H2,
                new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));

        dslContext.createTableIfNotExists(jooq.jooqobjects.tables.Expense.EXPENSE.getName())
                .column(jooq.jooqobjects.tables.Expense.EXPENSE.ID.getName(), H2DataType.INTEGER)
                .column(jooq.jooqobjects.tables.Expense.EXPENSE.DATE.getName(), H2DataType.DATE)
                .column(jooq.jooqobjects.tables.Expense.EXPENSE.AMOUNT.getName(), H2DataType.DECIMAL)
                .column(jooq.jooqobjects.tables.Expense.EXPENSE.REASON.getName(), H2DataType.VARCHAR)
                .column(jooq.jooqobjects.tables.Expense.EXPENSE.VAT.getName(), H2DataType.DECIMAL)
                .execute();
    }


    @After
    public void shutdownDatabase() {
        database.shutdown();
    }


    @Test
    public void testByDefaultNoExpenseIsAvailable() throws Exception {
        Connection connection = database.getConnection();
        DSLContext dslContext = DSL.using(connection, SQLDialect.H2, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));

        // mock the CurrencyConversionService so that it always returns 1 as conversion rate
        CurrencyConversionService mockedCurrencyConversionClass = mock(CurrencyConversionService.class);
        when(mockedCurrencyConversionClass.getConversionRate(any(CurrencyEnum.class)))
                .thenReturn(CompletableFuture.completedFuture(BigDecimal.valueOf(1)));

        ExpenseService expensesService = new ExpenseServiceImpl(
                new ExpenseRepositoryImpl(),
                mockedCurrencyConversionClass);

        List<Expense> allExpenses = expensesService.findAllExpenses(dslContext).toCompletableFuture().join();

        // check that by default no expenses are available in the DB
        assertTrue(allExpenses.isEmpty());
    }


    @Test
    public void testThatWhenSavingOneExpenseOneCanBeRetrieved() throws Exception {
        Connection connection = database.getConnection();
        DSLContext dslContext = DSL.using(connection, SQLDialect.H2, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));

        // mock the CurrencyConversionService so that it always returns 1 as conversion rate
        CurrencyConversionService mockedCurrencyConversionClass = mock(CurrencyConversionService.class);
        when(mockedCurrencyConversionClass.getConversionRate(any(CurrencyEnum.class)))
                .thenReturn(CompletableFuture.completedFuture(BigDecimal.valueOf(1)));

        ExpenseService expensesService = new ExpenseServiceImpl(
                new ExpenseRepositoryImpl(),
                mockedCurrencyConversionClass);

        // check that no expenses are available yet
        List<Expense> allExpenses = expensesService.findAllExpenses(dslContext).toCompletableFuture().join();
        assertTrue(allExpenses.isEmpty());

        // create a new expense to be stored
        ClientExpense newExpense = new ClientExpense();
        newExpense.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern(ClientExpense.DATE_FORMAT)));
        newExpense.setAmount("100.123456");
        newExpense.setReason("I can't even");

        expensesService.saveExpense(dslContext, newExpense).toCompletableFuture().join();

        allExpenses = expensesService.findAllExpenses(dslContext).toCompletableFuture().join();

        ExpenseWithCurrency newExpenseWithCurrency = newExpense.toExpenseWithCurrency();

        // check that we have retrieved just one expense
        assertTrue(allExpenses.size() == 1);

        // and finally check that the two expenses are equal
        Expense retrievedExpense = allExpenses.get(0);

        assertTrue(newExpenseWithCurrency.getDate().equals(retrievedExpense.getDate()));

        BigDecimal amountTimesVat = newExpenseWithCurrency.getAmount().multiply(ExpenseServiceImpl.UK_VAT_VALUE);
        BigDecimal amountWithVat = newExpenseWithCurrency.getAmount().add(amountTimesVat);
        assertTrue(amountWithVat.equals(retrievedExpense.getAmount()));

        assertTrue(newExpenseWithCurrency.getReason().equals(retrievedExpense.getReason()));
    }


}
