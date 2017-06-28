package io.warburton.ferry.service;

import io.warburton.ferry.model.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tw
 */
public interface TransactionService {

    Transaction get(UUID transactionId);

    Transaction create(UUID sourceId, UUID desinationId, BigDecimal amount);

}
