package org.daniel;

import org.daniel.repository.AccountRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages(getClass().getPackage().getName(), "com.fasterxml.jackson.jaxrs.json");

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new AccountRepository()).to(AccountRepository.class);
            }
        });
    }
}
