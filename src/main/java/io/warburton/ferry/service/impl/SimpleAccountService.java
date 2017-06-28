package io.warburton.ferry.service.impl;

import io.warburton.ferry.model.Account;
import io.warburton.ferry.service.AccountService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple, in memory Account Service.
 *
 * @author tw
 */
public final class SimpleAccountService implements AccountService {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    public SimpleAccountService() {
        Account tobie = new Account(UUID.fromString("502f82c0-9504-4cd4-990d-23059b258fdc"), "Tobie account", BigDecimal.valueOf(10_00));
        accounts.put(tobie.getId(), tobie);

        Account revolut = new Account(UUID.fromString("be86b7ef-002c-4eda-946e-939a4043cc16"), "Revolut account", BigDecimal.valueOf(1000_00));
        accounts.put(revolut.getId(), revolut);

        Account bank = new Account(UUID.fromString("c3868d05-064c-4e0c-9e55-88b096437127"), "Some big bank account", BigDecimal.valueOf(100000_00));
        accounts.put(bank.getId(), bank);
    }

    @Override
    public Account get(UUID id) {
        return accounts.get(id);
    }

}
