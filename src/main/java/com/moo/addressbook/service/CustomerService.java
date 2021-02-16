package com.moo.addressbook.service;

import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.domain.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    private final ICustomerRepository repository;

    @Autowired
    public CustomerService(ICustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Customer> getCustomersBySurname(String surname) {
        return repository.findBySurnameContainingIgnoreCase(surname);
    }
}
