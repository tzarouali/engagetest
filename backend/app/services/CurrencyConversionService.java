package services;

import model.CurrencyEnum;

import java.math.BigDecimal;
import java.util.concurrent.CompletionStage;

public interface CurrencyConversionService {

    /**
     * Retrieves the conversion rate between any currency supported by {@link CurrencyEnum}
     * and the GBP
     *
     * @return a {@link CompletionStage} that when completed successfully, will contain a BigDecimal object
     * with the conversion rate
     */
    CompletionStage<BigDecimal> getConversionRate(CurrencyEnum currency);
}
