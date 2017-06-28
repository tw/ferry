package io.warburton.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import io.warburton.ferry.model.Account;
import io.warburton.ferry.resource.AccountResource;
import io.warburton.ferry.service.AccountService;
import io.warburton.model.AccountTest;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author tw
 */
public class AccountResourceTest {
    private static final AccountService accountService = Mockito.mock(AccountService.class);
    private static Account account;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AccountResource(accountService))
            .build();

    @Before
    public void setup() {
        account = AccountTest.generateAccount();
        when(accountService.get(account.getId())).thenReturn(account);
    }

    @After
    public void tearDown() {
        Mockito.reset(accountService);
    }

    @Test
    public void testGet() {
        Response response = resources.target("/accounts/" + account.getId())
                .request()
                .get();
        assertEquals(OK.getStatusCode(), response.getStatus());

        Account entity = response.readEntity(Account.class);
        assertEquals(account, entity);
    }

}
