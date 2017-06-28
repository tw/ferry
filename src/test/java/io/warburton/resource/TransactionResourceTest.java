package io.warburton.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import io.warburton.ferry.model.Account;
import io.warburton.ferry.model.Transaction;
import io.warburton.ferry.resource.TransactionResource;
import io.warburton.ferry.resource.request.TransactionRequest;
import io.warburton.ferry.service.AccountService;
import io.warburton.ferry.service.TransactionService;
import io.warburton.ferry.service.impl.SimpleTransactionService;
import io.warburton.model.AccountTest;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

import static io.warburton.ferry.model.Transaction.TransactionState.CLEARED;
import static io.warburton.ferry.model.Transaction.TransactionState.DECLINED;
import static io.warburton.ferry.model.Transaction.TransactionStateReason.BALANCE;
import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author tw
 */
public class TransactionResourceTest {
    private static final AccountService accountService = Mockito.mock(AccountService.class);
    private static final TransactionService transactionService = new SimpleTransactionService(accountService);
    private static Account source;
    private static Account destination;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TransactionResource(transactionService))
            .build();

    @Before
    public void setup() {
        source = AccountTest.generateAccount();
        destination = AccountTest.generateAccount();

        when(accountService.get(source.getId())).thenReturn(source);
        when(accountService.get(destination.getId())).thenReturn(destination);
    }

    @After
    public void tearDown() {
        Mockito.reset(accountService);
    }

    @Test
    public void testGet() {
        Response response = resources.target("/transactions/" + UUID.randomUUID())
                .request()
                .get();
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGoodCreate() {
        TransactionRequest goodTransactionRequest = new TransactionRequest(source.getId(), destination.getId(),
                BigDecimal.valueOf(500));

        Response response;

        response = resources.target("/transactions")
                .request()
                .post(Entity.json(goodTransactionRequest));
        assertEquals(OK.getStatusCode(), response.getStatus());

        Transaction transaction = response.readEntity(Transaction.class);
        assertEquals(CLEARED, transaction.getState());
        assertEquals(goodTransactionRequest.getSourceAccountId(), transaction.getSourceAccountId());
        assertEquals(goodTransactionRequest.getDestinationAccountId(), transaction.getDestinationAccountId());
        assertEquals(goodTransactionRequest.getAmount(), transaction.getAmount());
    }

    @Test
    public void testBadCreate() {
        TransactionRequest badTransactionRequest = new TransactionRequest(source.getId(), destination.getId(),
                BigDecimal.valueOf(1001));

        Response response;

        response = resources.target("/transactions")
                .request()
                .post(Entity.json(badTransactionRequest));
        assertEquals(PAYMENT_REQUIRED.getStatusCode(), response.getStatus());

        Transaction transaction = response.readEntity(Transaction.class);
        assertEquals(DECLINED, transaction.getState());
        assertEquals(BALANCE, transaction.getStateReason());
        assertEquals(badTransactionRequest.getSourceAccountId(), transaction.getSourceAccountId());
        assertEquals(badTransactionRequest.getDestinationAccountId(), transaction.getDestinationAccountId());
        assertEquals(badTransactionRequest.getAmount(), transaction.getAmount());
    }

}
