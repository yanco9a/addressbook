package com.moo.addressbook;

import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.service.CustomerService;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@Profile("demo")
public class AddressBookDemoConfig {
    private static final Logger LOG = getLogger(AddressBookDemoConfig.class.getCanonicalName());

    @Bean
    public CommandLineRunner demo(CustomerService service) {
        return (args) -> {
            LOG.info("***************************************");
            LOG.info("Find Customer(s) by Surname: 'Belcher'");
            LOG.info("***************************************");
            List<Customer> customers = service.getCustomersBySurname("belcher");
            customers.forEach(customer -> LOG.info(customer.toString()));

            LOG.info("***************************************");
            LOG.info("Find Customer by Identifier: '1'");
            LOG.info("***************************************");
                Optional<Customer> customerById = service.getCustomerById(1L);
                customerById.ifPresent(customer -> LOG.info(customer.toString()));
        };
    }
}
