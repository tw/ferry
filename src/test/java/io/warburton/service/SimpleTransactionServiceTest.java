package io.warburton.service;

import io.warburton.ferry.model.Account;
import io.warburton.ferry.model.Transaction;
import io.warburton.ferry.service.AccountService;
import io.warburton.ferry.service.TransactionService;
import io.warburton.ferry.service.impl.SimpleTransactionService;
import io.warburton.model.AccountTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static io.warburton.ferry.model.Transaction.TransactionState.CLEARED;
import static io.warburton.ferry.model.Transaction.TransactionState.DECLINED;
import static io.warburton.ferry.model.Transaction.TransactionStateReason.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author tw
 */
public class SimpleTransactionServiceTest {

    private AccountService accountService = Mockito.mock(AccountService.class);
    private TransactionService transactionService = new SimpleTransactionService(accountService);
    private Account source;
    private Account destination;

    @Before
    public void before() {
        Mockito.reset(accountService);

        source = AccountTest.generateAccount();
        destination = AccountTest.generateAccount();

        when(accountService.get(source.getId())).thenReturn(source);
        when(accountService.get(destination.getId())).thenReturn(destination);
    }

    @Test
    public void testCreateMissingAccount() {
        Transaction transaction;

        transaction = transactionService.create(source.getId(), UUID.randomUUID(), BigDecimal.valueOf(100));
        assertEquals(DECLINED, transaction.getState());
        assertEquals(ACCOUNT_NOT_FOUND, transaction.getStateReason());

        transaction = transactionService.create(UUID.randomUUID(), destination.getId(), BigDecimal.valueOf(100));
        assertEquals(DECLINED, transaction.getState());
        assertEquals(ACCOUNT_NOT_FOUND, transaction.getStateReason());
    }

    @Test
    public void testInvalidAmount() {
        Transaction transaction;

        transaction = transactionService.create(source.getId(), destination.getId(), BigDecimal.ZERO);
        assertEquals(DECLINED, transaction.getState());
        assertEquals(INVALID_TRANSACTION_AMOUNT, transaction.getStateReason());

        transaction = transactionService.create(source.getId(), destination.getId(), BigDecimal.ONE.negate());
        assertEquals(DECLINED, transaction.getState());
        assertEquals(INVALID_TRANSACTION_AMOUNT, transaction.getStateReason());
    }

    @Test
    public void testBalance() {
        Transaction transaction;

        transaction = transactionService.create(source.getId(), destination.getId(), source.getBalance().add(BigDecimal.ONE));
        assertEquals(DECLINED, transaction.getState());
        assertEquals(BALANCE, transaction.getStateReason());
    }

    @Test
    public void testOk() {
        Transaction transaction;

        BigDecimal sourceExpected = source.getBalance().subtract(BigDecimal.ONE);
        BigDecimal destinationExpected = destination.getBalance().add(BigDecimal.ONE);

        transaction = transactionService.create(source.getId(), destination.getId(), BigDecimal.ONE);
        assertEquals(CLEARED, transaction.getState());
        assertEquals(null, transaction.getStateReason());

        assertEquals(sourceExpected, source.getBalance());
        assertEquals(destinationExpected, destination.getBalance());

        assertEquals(transaction, transactionService.get(transaction.getId()));
    }

}
