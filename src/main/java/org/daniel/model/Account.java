package org.daniel.model;

import java.math.BigDecimal;
import java.util.Currency;

public class Account {
    private long id;
    private String name;
    private Currency currency;
    private BigDecimal money;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setName(BigDecimal money) {
        this.money = money;
    }
}
