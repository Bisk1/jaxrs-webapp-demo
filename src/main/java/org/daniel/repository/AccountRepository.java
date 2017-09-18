package org.daniel.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.daniel.model.Account;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Named
public class AccountRepository {
    private Accounts accounts;
    private ObjectMapper objectMapper = new ObjectMapper();
    private File dataFile = new File("data.json");

    private long nextId = 1;

    public AccountRepository() {
        readDatabase();
    }

    public Optional<Account> lookup(long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Collection<Account> getAll() {
        return accounts.getMap().values();
    }

    public Account create(Account account) {
        account.setId(getNextId());
        accounts.put(account.getId(), account);
        saveDatabase();
        return account;
    }

    public Account update(Account account) {
        accounts.put(account.getId(), account);
        saveDatabase();
        return account;
    }

    private void readDatabase() {
        try {
            accounts = dataFile.exists() ? objectMapper.readValue(dataFile, Accounts.class) : new Accounts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDatabase() {
        try {
            objectMapper.writeValue(dataFile, accounts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getNextId() {
        return nextId++;
    }
}
