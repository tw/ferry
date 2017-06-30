package io.warburton.ferry.model;

import io.warburton.ferry.exception.UpdateBalanceException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tw
 */
public class Account {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private UUID id;
    private String description;
    private BigDecimal balance;

    public Account(UUID id, String description, BigDecimal balance) {
        this.id = id;
        this.description = description;
        this.balance = balance;
    }

    public Account() {
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void lock() {
        lock.writeLock().lock();
    }

    public void unlock() {
        lock.writeLock().unlock();
    }

    public void updateBalance(BigDecimal amount) throws UpdateBalanceException {
        if (amount.compareTo(BigDecimal.ZERO) == -1 && amount.negate().compareTo(balance) == 1) { // if lt 0 and gt balance
            throw new UpdateBalanceException("Not enough funds available to subtract " + amount + " from " + balance);
        } else {
            balance = balance.add(amount);
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "lock=" + lock +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (description != null ? !description.equals(account.description) : account.description != null) return false;
        return balance != null ? balance.equals(account.balance) : account.balance == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

}
