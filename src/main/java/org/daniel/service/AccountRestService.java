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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Response patchAccount(JsonPatch patch, @PathParam("id") long accountId) throws JsonProcessingException {
        Optional<Account> beforePatchOpt = accountRepository.lookup(accountId);
        if (!beforePatchOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Account beforePatch = beforePatchOpt.get();

        try {
            JsonNode beforePatchNode = objectMapper.valueToTree(beforePatch);
            JsonNode afterPatchNode = patch.apply(beforePatchNode);
            Account afterPatch = objectMapper.treeToValue(afterPatchNode, Account.class);
            if (!beforePatch.getId().equals(afterPatch.getId())) { // forbidden to modify id
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Id cannot be modified")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                        .build();
            }
            Account updated = accountRepository.update(afterPatch);
            return Response.status(Response.Status.OK).entity(updated).build();
        } catch (JsonPatchException e) {
            // this exception means that a 'test' operation of patch evaluated to false
            // it is not an error, but the object remains unchanged
            return Response.status(Response.Status.CONFLICT).entity(beforePatch).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Collection<Account> allAccounts = accountRepository.getAll();

        List<String> allNames = allAccounts.stream()
                .map(Account::getName)
                .collect(Collectors.toList());
        System.out.println("Getting all the accounts, names: " + String.join(", ", allNames));
        return Response.status(Response.Status.OK).entity(allAccounts).build();
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}