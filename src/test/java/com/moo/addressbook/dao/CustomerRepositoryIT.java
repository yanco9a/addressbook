package com.moo.addressbook.dao;

import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.domain.CustomerAddress;
import com.moo.addressbook.domain.ICustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ActiveProfiles("test")
class CustomerRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ICustomerRepository repository;

    @DisplayName("findBySurnameContainingIgnoreCase() returns all customers matched by surname (case-insensitive).")
    @Test
    void findBySurnameContainingIgnoreCaseTest() {
        // given
        String requestedSurname = "belcher";
        CustomerAddress address = new CustomerAddress(1L, "addy1", "addy2", "ABC XYZ");
        String mixedCaseSurname = "BelCher";
        Customer customerOne = new Customer(1L, "Miss", "Tina",
                mixedCaseSurname, "00000000000");
        String upperCasedSurname = requestedSurname.toUpperCase();
        Customer customerTwo = new Customer(2L, "Miss", "Tina",
                upperCasedSurname, "00000000000");
        // And
        Customer unmatchedCustomer = new Customer(3L, "Mr", "Gene",
                "unmatchedCustomer", "11111111111");
        customerOne.addAddressToCustomer(address);
        entityManager.merge(customerOne);
        entityManager.flush();
        customerTwo.addAddressToCustomer(address);
        entityManager.merge(customerTwo);
        entityManager.flush();
        unmatchedCustomer.addAddressToCustomer(address);
        entityManager.merge(unmatchedCustomer);
        entityManager.flush();

        // when
        List<Customer> actual = repository.findBySurnameContainingIgnoreCase("belcher");

        // then
        Assertions.assertThat(actual.size()).isEqualTo(2);
    }
}
