package io.warburton.model;

import io.warburton.ferry.exception.UpdateBalanceException;
import io.warburton.ferry.model.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author tw
 */
public class AccountTest {

    @Test
    public void testPositive() throws UpdateBalanceException {
        Account account = generateAccount();
        account.updateBalance(BigDecimal.valueOf(501));

        assertEquals(BigDecimal.valueOf(1501), account.getBalance());
    }

    @Test
    public void testNegative() throws UpdateBalanceException {
        Account account = generateAccount();
        account.updateBalance(BigDecimal.valueOf(501).negate());

        assertEquals(BigDecimal.valueOf(499), account.getBalance());
    }

    @Test(expected = UpdateBalanceException.class)
    public void testTooMuch() throws UpdateBalanceException {
        Account account = generateAccount();
        account.updateBalance(BigDecimal.valueOf(1001).negate());
    }

    public static Account generateAccount() {
        return new Account(UUID.randomUUID(), UUID.randomUUID().toString(), BigDecimal.valueOf(1000));
    }

}
