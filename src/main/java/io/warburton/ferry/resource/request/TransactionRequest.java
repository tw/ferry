package io.warburton.ferry.resource.request;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tw
 */
public class TransactionRequest {

    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;

    public TransactionRequest(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public TransactionRequest() {
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

}
