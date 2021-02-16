package com.moo.addressbook.api.controller;

import com.moo.addressbook.api.exception.CustomerNotFoundException;
import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.service.ICustomerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Validated
@RestController
@RequestMapping(value = "/api/v1/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    private static final Logger LOG = getLogger(CustomerController.class.getCanonicalName());
    private final ICustomerService service;

    @Autowired
    public CustomerController(ICustomerService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id")
                                                    @Positive(message = "Customer id must be greater than 0") Long id) {
            LOG.info(format("preparing to return customer by id: %s", id));
            Customer customer = service.getCustomerById(id).orElseThrow(() -> new CustomerNotFoundException(id));
            LOG.info(format("customer retrieved. Customer: %s", customer));
            return ResponseEntity.ok(customer);
        }

        @GetMapping("/search")
        public ResponseEntity<List<Customer>> getCustomersBySurname(
                @RequestParam("surname")
                @Size(min = 2, message = "Customer surname must be at least two characters length") String surname) {
                LOG.info(format("preparing to return customer(s) by the surname: %s", surname));
                List<Customer> customers = service.getCustomersBySurname(surname);
                customers.forEach(customer -> LOG.info(format("Customer: %s", customer)));
                if (customers.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(customers);
            }
        }
