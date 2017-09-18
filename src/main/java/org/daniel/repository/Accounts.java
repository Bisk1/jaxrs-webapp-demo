package org.daniel.repository;

import org.daniel.model.Account;

import java.util.HashMap;
import java.util.Map;

// This is a wrapper for Account database for easier access and storage
public class Accounts {

    private long nextId = 1;

    // database is represented as an in-memory hashmap
    private Map<Long, Account> map = new HashMap<>();

    public void generateIdAndSave(Account account) {
        long id = getNextId();
        account.setId(id);
        map.put(id, account);
    }

    public void save(long id, Account account) {
        map.put(id, account);
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


    private long getNextId() {
        return nextId++;
    }

}
