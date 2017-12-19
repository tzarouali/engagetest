package model;

public enum CurrencyEnum {

    GBP("GBP"),
    EUR("EUR");

    private String currency;


    CurrencyEnum(String currency) {
        this.currency = currency;
    }


    @Override
    public String toString() {
        return this.currency;
    }
}
