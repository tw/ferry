package io.warburton.ferry.service.impl;

import io.warburton.ferry.exception.UpdateBalanceException;
import io.warburton.ferry.model.Account;
import io.warburton.ferry.model.AccountPair;
import io.warburton.ferry.model.Transaction;
import io.warburton.ferry.model.Transaction.TransactionState;
import io.warburton.ferry.model.Transaction.TransactionStateReason;
import io.warburton.ferry.service.AccountService;
import io.warburton.ferry.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import static io.warburton.ferry.model.Transaction.TransactionState.CLEARED;
import static io.warburton.ferry.model.Transaction.TransactionState.DECLINED;

/**
 * @author tw
 */
public final class SimpleTransactionService implements TransactionService {

    private AccountService accountService;
    private Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();
    private Map<AccountPair, Semaphore> semaphores = new ConcurrentHashMap<>();

    public SimpleTransactionService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Transaction get(UUID transactionId) {
        return transactions.get(transactionId);
    }

    @Override
    public Transaction create(UUID sourceId, UUID desinationId, BigDecimal amount) {
        Account source = accountService.get(sourceId);
        Account destination = accountService.get(desinationId);

        TransactionState state;
        TransactionStateReason reason = null;

        if (source == null || destination == null) {
            state = DECLINED;
            reason = TransactionStateReason.ACCOUNT_NOT_FOUND;
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            state = DECLINED;
            reason = TransactionStateReason.INVALID_TRANSACTION_AMOUNT;
        } else {
            AccountPair pair = new AccountPair(source, destination);
            try {
                lock(pair);

                source.updateBalance(amount.negate());
                destination.updateBalance(amount);

                state = CLEARED;
            } catch (UpdateBalanceException e) {
                state = DECLINED;
                reason = TransactionStateReason.BALANCE;
            } finally {
                unlock(pair);
            }
        }

        Transaction transaction = new Transaction(UUID.randomUUID(),
                sourceId, desinationId, amount,
                state, reason, LocalDateTime.now(ZoneId.of("UTC")));
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    private void lock(AccountPair pair) {
        Semaphore semaphore = semaphores.computeIfAbsent(pair, p -> new Semaphore(1));
        try {
            semaphore.acquire();

            pair.getLeft().lock();
            pair.getRight().lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void unlock(AccountPair pair) {
        Semaphore semaphore = semaphores.get(pair);
        try {
            pair.getLeft().unlock();
            pair.getRight().unlock();
        } finally {
            semaphore.release();
        }
    }
}
