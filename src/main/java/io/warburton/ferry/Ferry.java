package io.warburton.ferry;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.warburton.ferry.resource.AccountResource;
import io.warburton.ferry.resource.TransactionResource;
import io.warburton.ferry.service.AccountService;
import io.warburton.ferry.service.impl.SimpleAccountService;
import io.warburton.ferry.service.impl.SimpleTransactionService;
import io.warburton.ferry.service.TransactionService;

/**
 * @author tw
 */
public class Ferry extends Application<FerryConfiguration> {

    public static void main(String[] args) throws Exception {
        new Ferry().run(args);
    }

    @Override
    public String getName() {
        return "ferry";
    }

    @Override
    public void initialize(Bootstrap<FerryConfiguration> bootstrap) {
    }

    @Override
    public void run(FerryConfiguration configuration, Environment environment) {
        AccountService accountService = new SimpleAccountService();
        TransactionService transactionService = new SimpleTransactionService(accountService);

        final AccountResource accountResource = new AccountResource(accountService);
        environment.jersey().register(accountResource);

        final TransactionResource transactionResource = new TransactionResource(transactionService);
        environment.jersey().register(transactionResource);
    }

}

