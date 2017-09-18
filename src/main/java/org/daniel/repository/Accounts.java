package org.daniel.repository;

import org.daniel.model.Account;

import java.util.HashMap;
import java.util.Map;

// This is a wrapper for Account database for easier access and storage
public class Accounts {

    private long lastId = 0;

    // database is represented as an in-memory hashmap
    private Map<Long, Account> map = new HashMap<>();

    public Account generateIdAndSave(Account account) {
        long id = nextId();
        Account saved = new Account.Builder()
                .withId(id)
                .withName(account.getName())
                .withCurrency(account.getCurrency())
                .withMoney(account.getMoney())
                .build();
        save(saved);
        return saved;
    }

    public void save(Account account) {
        if (account.getId() == null) {
            throw new RuntimeException("Account must have id to be saved");
        }
        map.put(account.getId(), account);
    }

    public Account get(long id) {
        return map.get(id);
    }

    public void setMap(Map<Long, Account> map) {
        this.map = map;
    }

    public Map<Long, Account> getMap() {
        return map;
    }


    private long nextId() {
        return ++lastId;
    }

    // for serialization and deserialization
    public long getLastId() {
        return lastId;
    }
    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

}
