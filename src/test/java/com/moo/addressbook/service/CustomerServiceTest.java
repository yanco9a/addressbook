package com.moo.addressbook.service;

import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.domain.CustomerAddress;
import com.moo.addressbook.domain.ICustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private ICustomerRepository repository;

    @Test
    @DisplayName("findById 1L when customer with id==1L exists")
    void testFindByIdSuccessTest() {
        // Given
        CustomerAddress addressOne = new CustomerAddress(1L, "addy1", "addy11", "ABC");
        CustomerAddress addressTwo = new CustomerAddress(2L, "addy2", "addy22", "XYZ");
        Customer customer = new Customer(1L, "Miss", "Tina", "Belcher", "00000000000");
        customer.addAddressToCustomer(addressOne);
        customer.addAddressToCustomer(addressTwo);
        doReturn(Optional.of(customer)).when(repository).findById(1l);

        // When
        Optional<Customer> actualResponse = service.getCustomerById(1l);

        // Then
        assertTrue(actualResponse.isPresent());
        assertSame(actualResponse.get(), customer);
        assertEquals(actualResponse.get().getCustomerAddresses().size(), 2);
    }

    @Test
    @DisplayName("findBySurname Thompson when customer with surname==THOMPSON exists")
    void testFindBySurnameSuccessTest() {
        // Given
        CustomerAddress addressOne = new CustomerAddress(1L, "addy1", "addy11", "ABC");
        Customer customer = new Customer(1L, "Miss", "Tina", "THOMPSON", "00000000000");
        customer.addAddressToCustomer(addressOne);
        doReturn(List.of(customer)).when(repository).findBySurnameContainingIgnoreCase("Thompson");

        // When
        List<Customer> actualResponse = service.getCustomersBySurname("Thompson");

        // Then
        assertEquals(actualResponse.size(), 1);
        assertSame(actualResponse.get(0), customer);
        assertEquals(actualResponse.get(0).getCustomerAddresses().size(), 1);
    }

    @Test
    @DisplayName("findById when customer does not exists returns empty")
    void testFindByIdEmptyTest() {
        // Given
        doReturn(Optional.empty()).when(repository).findById(1l);

        // When
        Optional<Customer> actualResponse = service.getCustomerById(1l);

        // Then
        assertFalse(actualResponse.isPresent());
    }

    @Test
    @DisplayName("findBySurname when customer does not exists returns empty")
    void testFindBySurnameEmptyTest() {
        // Given
        doReturn(emptyList()).when(repository).findBySurnameContainingIgnoreCase("Thompson");

        // When
        List<Customer> actualResponse = service.getCustomersBySurname("Thompson");

        // Then
        assertTrue(actualResponse.isEmpty());
    }
}
