package io.warburton.ferry.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author tw
 */
public class Transaction {

    private UUID id;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;
    private TransactionState state;
    private TransactionStateReason stateReason;
    private LocalDateTime created;

    public Transaction(UUID id, UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount,
                       TransactionState state, TransactionStateReason stateReason, LocalDateTime created) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.state = state;
        this.stateReason = stateReason;
        this.created = created;
    }

    public Transaction() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionState getState() {
        return state;
    }

    public TransactionStateReason getStateReason() {
        return stateReason;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public enum TransactionState {
        CLEARED, DECLINED
    }

    public enum TransactionStateReason {
        ACCOUNT_NOT_FOUND("Unable to find source or destination account."),
        INVALID_TRANSACTION_AMOUNT("Transaction amount invalid, must be one or greater."),
        BALANCE("Source account balance must equal or exceed transaction amount");

        private String message;

        TransactionStateReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", amount=" + amount +
                ", state=" + state +
                ", stateReason='" + stateReason + '\'' +
                ", created=" + created +
                '}';
    }
}
