package com.moo.addressbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_ADDRESS")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String streetName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    private String postcode;

    @ManyToMany(mappedBy = "customerAddresses")
    @JsonIgnore
    @MapKey(name = "id")
    private Map<Long, Customer> customers = new HashMap<>();

    public CustomerAddress() {
    }

    public CustomerAddress(Long id, String streetName, String city, String postcode) {
        this.id = id;
        this.streetName = streetName;
        this.city = city;
        this.postcode = postcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Map<Long, Customer> getCustomers() {
        return new HashMap<>(customers);
    }

    public void addCustomerToAddress(Customer customer) {
        if (customers.containsValue(customer))
            return;
        customers.put(customer.getId(), customer);
        customer.addAddressToCustomer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerAddress)) return false;
        CustomerAddress address = (CustomerAddress) o;
        return getId().equals(address.getId()) &&
                getStreetName().equals(address.getStreetName()) &&
                getCity().equals(address.getCity()) &&
                getPostcode().equals(address.getPostcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStreetName(), getCity(), getPostcode());
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                "streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
