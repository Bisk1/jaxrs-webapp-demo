package org.daniel;

import org.daniel.repository.AccountRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AccountRepository.class).to(AccountRepository.class);
    }
}