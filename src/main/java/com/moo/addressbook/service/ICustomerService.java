package com.moo.addressbook.service;

import com.moo.addressbook.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    Optional<Customer> getCustomerById(Long id);
    List<Customer> getCustomersBySurname(String surname);
}
