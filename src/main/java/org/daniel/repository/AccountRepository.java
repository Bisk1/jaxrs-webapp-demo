package org.daniel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daniel.model.Account;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Singleton
public class AccountRepository {
    private Accounts accounts;
    private ObjectMapper objectMapper = new ObjectMapper();
    private File dataFile = new File("data.json");

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
        accounts.generateIdAndSave(account);
        saveDatabase();
        return account;
    }

    public Account update(Account account) {
        accounts.save(account.getId(), account);
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

}
