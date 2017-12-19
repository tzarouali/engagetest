package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;


/**
 * Represents an expense received from the client
 */
public class ClientExpense {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    private String date;
    private String amount;
    private String reason;


    /**
     * Checks whether the amount of the expense is a number or not
     *
     * @return True if the string containing the amount is a number
     * and False otherwise
     */
    private boolean amountIsNumber() {
        boolean isNumber = false;
        try {
            new BigDecimal(amount);
            isNumber = true;
        } catch (Exception e) {
        }
        return isNumber;
    }


    /**
     * Parses the (possibly existing) currency contained in the string representing the amount
     *
     * @return CurrencyEnum representing the currency of the amount
     */
    private CurrencyEnum getCurrency() {
        CurrencyEnum currency;
        if (amountIsNumber()) {
            // by default we're dealing with Pounds
            currency = CurrencyEnum.GBP;
        } else {
            Optional<CurrencyEnum> matchingCurrency = Arrays.stream(CurrencyEnum.values())
                    .filter(curr -> amount.endsWith(curr.toString()))
                    .findFirst();
            if (matchingCurrency.isPresent()) {
                currency = matchingCurrency.get();
            } else {
                throw new IllegalArgumentException("Unsupported currency");
            }
        }
        return currency;
    }


    /**
     * Converts this {@link ClientExpense} to an {@link ExpenseWithCurrency} object containing
     * the currency of the expense
     *
     * @return ExpenseWithCurrency object
     */
    public ExpenseWithCurrency toExpenseWithCurrency() {
        ExpenseWithCurrency expense = new ExpenseWithCurrency();
        if (amountIsNumber()) {
            expense.setCurrency(CurrencyEnum.GBP);
            expense.setAmount(parseAmount(amount));
        } else {
            CurrencyEnum currency = getCurrency();
            expense.setCurrency(currency);
            String amountStrWithoutCurrency = amount.substring(0, amount.indexOf(currency.toString())).trim();
            expense.setAmount(parseAmount(amountStrWithoutCurrency));
        }
        expense.setDate(parseDate());
        expense.setReason(reason);
        return expense;
    }


    /**
     * Parses a string containing a numeric amount
     *
     * @param amount the string with the amount
     * @return a {@link BigDecimal} with the amount parsed
     */
    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing the amount");
        }
    }


    /**
     * Parses the date of the client expense
     *
     * @return a {@link LocalDate} representing the expense date
     */
    private LocalDate parseDate() {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error parsing the expense date");
        }
    }


    /**
     * Checks whether the string containing the amount of this {@link ClientExpense}
     * is a value in Pounds or not
     *
     * @return True if the amount is in Pounds and False otherwise
     */
    public boolean isInPounds() {
        return CurrencyEnum.GBP == getCurrency();
    }


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }

}
