package utils;

import com.google.inject.AbstractModule;
import repositories.ExpenseRepository;
import repositories.impl.ExpenseRepositoryImpl;
import services.CurrencyConversionService;
import services.ExpenseService;
import services.impl.CurrencyConversionServiceImpl;
import services.impl.ExpenseServiceImpl;

public class GuiceDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExpenseService.class).to(ExpenseServiceImpl.class);
        bind(CurrencyConversionService.class).to(CurrencyConversionServiceImpl.class);
        bind(ExpenseRepository.class).to(ExpenseRepositoryImpl.class);
    }

}
