package io.warburton.ferry.resource;

import io.warburton.ferry.model.Account;
import io.warburton.ferry.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

/**
 * @author tw
 */
@Path("accounts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AccountResource {

    private AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID accountId) {
        Account account = accountService.get(accountId);
        if (account == null) {
            return Response.status(NOT_FOUND).build();
        } else {
            return Response.status(OK).entity(account).build();
        }
    }

}
