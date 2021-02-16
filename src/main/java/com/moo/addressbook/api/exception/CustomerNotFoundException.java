package com.moo.addressbook.api.exception;

import static java.lang.String.format;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(format("Customer with id %d not found", id));
    }
}