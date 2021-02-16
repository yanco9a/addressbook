package com.moo.addressbook.api.controller;

import com.moo.addressbook.TestUtils.JsonHelper;
import com.moo.addressbook.domain.Customer;
import com.moo.addressbook.domain.CustomerAddress;
import com.moo.addressbook.domain.ICustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static java.util.List.copyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@ActiveProfiles("test")
class CustomerControllerITest {
    private CustomerAddress address;
    private Customer customer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICustomerRepository repository;

    @Test
    @DisplayName("GET /api/v1/customers/1 when 200 OK")
    void getCustomerById200Test() throws Exception {
        // Given
        address = new CustomerAddress(1L,
                "addy1",
                "addy2",
                "ABC XYZ");
        customer = new Customer(1L,
                "Miss",
                "Tina",
                "Belcher",
                "00000000000");
        customer.addAddressToCustomer(address);
        repository.save(customer);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(customer.getTitle())))
                .andExpect(jsonPath("$.firstname", is(customer.getFirstname())))
                .andExpect(jsonPath("$.surname", is(customer.getSurname())))
                .andExpect(jsonPath("$.telephoneNumber", is(customer.getTelephoneNumber())))
                .andExpect(jsonPath("$.customerAddresses[0].id", is(1)))
                .andExpect(jsonPath("$.customerAddresses[0].streetName", is(
                        getCustomerAddress(customer, 0).getStreetName())))
                .andExpect(jsonPath("$.customerAddresses[0].city", is(
                        getCustomerAddress(customer, 0).getCity())))
                .andExpect(jsonPath("$.customerAddresses[0].postcode", is(
                        getCustomerAddress(customer, 0).getPostcode())))
                .andExpect(status().isOk())
                .andReturn();
        // And
        String response = result.getResponse().getContentAsString();
        Customer actualResponse = JsonHelper.readObjectFromString(response, Customer.class);
        assertEquals(customer, actualResponse);

        // And
        assertEquals(customer.toString(), actualResponse.toString());

        // And
        int expectedCode = actualResponse.hashCode();
        assertEquals(expectedCode, customer.hashCode());
        assertEquals(expectedCode, actualResponse.hashCode());
    }

    @Test
    @DisplayName("GET /api/v1/customers/search?surname=beLCHEr when 200 OK")
    void getCustomersBySurname200Test() throws Exception {
        // Given
        address = new CustomerAddress(1L, "addy1", "addy2", "ABC XYZ");
        String requestedSurname = "Belcher";
        Customer customerOne = new Customer(1L,
                "Miss",
                "Tina",
                requestedSurname,
                "00000000000");
        customerOne.addAddressToCustomer(address);
        repository.save(customerOne);

        Customer customerTwo = new Customer(2L,
                "Mr",
                "Gene",
                requestedSurname.toUpperCase(),
                "11111111111");
        customerTwo.addAddressToCustomer(address);
        repository.save(customerTwo);
        Customer unRequestedCustomer = new Customer(3L,
                "title",
                "firstname",
                "UNREQUESTED",
                "22222222222");
        unRequestedCustomer.addAddressToCustomer(address);
        repository.save(unRequestedCustomer);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/search")
                .param("surname", requestedSurname)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is(customerOne.getTitle())))
                .andExpect(jsonPath("$[0].firstname", is(customerOne.getFirstname())))
                .andExpect(jsonPath("$[0].surname", is(customerOne.getSurname())))
                .andExpect(jsonPath("$[0].telephoneNumber", is(customerOne.getTelephoneNumber())))
                .andExpect(jsonPath("$[0].customerAddresses[0].id", is(1)))
                .andExpect(jsonPath("$[0].customerAddresses[0].streetName", is(
                        getCustomerAddress(customerOne, 0).getStreetName())))
                .andExpect(jsonPath("$[0].customerAddresses[0].city", is(
                        getCustomerAddress(customerOne, 0).getCity())))
                .andExpect(jsonPath("$[0].customerAddresses[0].postcode", is(
                        getCustomerAddress(customerOne, 0).getPostcode())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is(customerTwo.getTitle())))
                .andExpect(jsonPath("$[1].firstname", is(customerTwo.getFirstname())))
                .andExpect(jsonPath("$[1].surname", is(customerTwo.getSurname())))
                .andExpect(jsonPath("$[1].telephoneNumber", is(customerTwo.getTelephoneNumber())))
                .andExpect(jsonPath("$[1].customerAddresses[0].id", is(1)))
                .andExpect(jsonPath("$[1].customerAddresses[0].streetName", is(
                        getCustomerAddress(customerTwo, 0).getStreetName())))
                .andExpect(jsonPath("$[1].customerAddresses[0].city", is(
                        getCustomerAddress(customerTwo, 0).getCity())))
                .andExpect(jsonPath("$[1].customerAddresses[0].postcode", is(
                        getCustomerAddress(customerTwo, 0).getPostcode())))
                .andExpect(status().isOk())
                .andReturn();
        // And
        String response = result.getResponse().getContentAsString();

        List<Customer> actualResponse = JsonHelper.readListFromString(response, Customer.class);
        assertEquals(2, actualResponse.size());
        assertTrue(actualResponse.get(0).getSurname().equalsIgnoreCase("belcher"));
        assertTrue(actualResponse.get(1).getSurname().equalsIgnoreCase("belcher"));
    }


    @Test
    @DisplayName("GET /api/v1/customers/search?surname=noOne when 204 NO CONTENT")
    void getCustomersBySurname204Test() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/search")
                .param("surname", "noOne")
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/customers/99 when 404 NOT FOUND")
    void getCustomerById404Test() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/99")
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Customer with id 99 not found")))
                .andExpect(jsonPath("$.description", is("uri=/api/v1/customers/99")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/customers/0 when 400 BAD REQUEST")
    void getCustomerById400Test() throws Exception {
        // When
        String notPostiveNumeric = String.valueOf(0l);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/" + notPostiveNumeric)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", containsString("Customer id must be greater than 0")))
                .andExpect(jsonPath("$.description", is("uri=/api/v1/customers/0")))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("GET /api/v1/customers/search?surname=  when 400 BAD REQUEST")
    void getCustomersBySurname400Test() throws Exception {
        // When
        String surnameTooShort = "s";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/customers/search")
                .param("surname", surnameTooShort)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", containsString("Customer surname must be at least two characters length")))
                .andExpect(jsonPath("$.description", is("uri=/api/v1/customers/search")))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private CustomerAddress getCustomerAddress(Customer customer, int pos) {
        return copyOf(customer.getCustomerAddresses()).get(pos);
    }
}