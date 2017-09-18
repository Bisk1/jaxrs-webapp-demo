package org.daniel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daniel.model.Account;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

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
        Account created = accounts.generateIdAndSave(account);
        saveDatabase();
        return created;
    }

    public Account update(Account account) {
        accounts.save(account);
        saveDatabase();
        return account;
    }

    private void readDatabase() {
        withIOExceptionWrapper(
                () -> accounts = dataFile.exists() ? objectMapper.readValue(dataFile, Accounts.class) : new Accounts()
        );
    }

    private void saveDatabase() {
        withIOExceptionWrapper(
                () -> objectMapper.writeValue(dataFile, accounts)
        );
    }

    private void withIOExceptionWrapper(MyRunnable runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface MyRunnable {
        void run() throws IOException;
    }
}
