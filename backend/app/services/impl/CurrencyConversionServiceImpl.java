package services.impl;

import model.CurrencyEnum;
import play.Logger;
import play.libs.ws.WSClient;
import services.CurrencyConversionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.CompletionStage;

public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private WSClient wsClient;


    @Inject
    public CurrencyConversionServiceImpl(WSClient wsClient) {
        this.wsClient = wsClient;
    }


    @Override
    public CompletionStage<BigDecimal> getConversionRate(CurrencyEnum currency) {
        Logger.info("Calling https://api.fixer.io to retrieve currency conversion rate for " + currency);
        return wsClient
                .url("https://api.fixer.io/latest?base=" + currency + "&symbols=" + CurrencyEnum.GBP)
                .get()
                .thenApply(response -> {
                    String conversionRateStr = response.asJson().get("rates").get(CurrencyEnum.GBP.toString()).asText();
                    return new BigDecimal(conversionRateStr);
                });
    }
}
