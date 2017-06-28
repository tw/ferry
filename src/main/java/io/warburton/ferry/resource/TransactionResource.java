package io.warburton.ferry.resource;

import io.dropwizard.validation.Validated;
import io.warburton.ferry.model.Transaction;
import io.warburton.ferry.resource.request.TransactionRequest;
import io.warburton.ferry.service.TransactionService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

/**
 * @author tw
 */
@Path("transactions")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class TransactionResource {

    private TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID transactionId) {
        Transaction transaction = transactionService.get(transactionId);
        if (transaction == null) {
            return Response.status(NOT_FOUND).build();
        } else {
            return Response.status(OK).entity(transaction).build();
        }
    }

    @POST
    public Response create(@Valid @Validated TransactionRequest request) {
        Transaction transaction = transactionService.create(
                request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount());

        return Response
                .status(transaction.getState().equals(Transaction.TransactionState.CLEARED) ? OK : PAYMENT_REQUIRED) // TODO: better status code needed
                .entity(transaction)
                .build();
    }

}
