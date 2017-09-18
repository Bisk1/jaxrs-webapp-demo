package org.daniel.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;
import java.util.Currency;

@JsonDeserialize(builder = Account.Builder.class)
public class Account {
    private Long id;
    private String name;
    private Currency currency;
    private BigDecimal money;

    private Account(Long id, String name, Currency currency, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.money = money;
    }

    public Long getId() {
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

    @JsonPOJOBuilder
    public static class Builder {

        private Long id;
        private String name;
        private Currency currency;
        private BigDecimal money;


        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder withMoney(BigDecimal money) {
            this.money = money;
            return this;
        }

        public Account build() {
            if (name == null) {
                throw new RuntimeException("name must be set");
            }
            if (currency == null) {
                throw new RuntimeException("currency must be set");
            }
            if (money == null) {
                throw new RuntimeException("money must be set");
            }
            return new Account(id, name, currency, money);
        }
    }
}
