package org.daniel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.daniel.model.Account;
import org.daniel.repository.AccountRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

@Path("/v1/accounts/account")
public class AccountRestService {

    @Inject
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@PathParam("id") long accountId) {
        Optional<Account> result = accountRepository.lookup(accountId);
        if (result.isPresent()) {
            return Response.status(Response.Status.OK).entity(result.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        Account created = accountRepository.create(account);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchAccount(JsonPatch patch, @PathParam("id") long accountId) {
        Optional<Account> beforePatchOpt = accountRepository.lookup(accountId);
        if (!beforePatchOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Account beforePatch = beforePatchOpt.get();
        try {
            JsonNode beforePatchNode = objectMapper.valueToTree(beforePatch);
            JsonNode afterPatchNode = patch.apply(beforePatchNode);
            Account afterPatch = objectMapper.treeToValue(afterPatchNode, Account.class);
            if (beforePatch.getId() != afterPatch.getId()) { // forbidden to modify id
                return Response.status(Response.Status.BAD_REQUEST).entity("Id cannot be modified").build();
            }
            accountRepository.update(afterPatch);
            return Response.status(Response.Status.OK).entity(afterPatch).build();
        } catch (JsonPatchException | JsonProcessingException e) { // it means patch request was invalid
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Patch request: " + e.getMessage()).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Collection<Account> result = accountRepository.getAll();
        return Response.status(Response.Status.OK).entity(result).build();
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}