package com.moo.addressbook.domain;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private String firstname;

    @Basic(optional = false)
    @Column(nullable = false)
    private String surname;

    @Basic
    @Column
    private String telephoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "C_A",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID"))
    private Set<CustomerAddress> customerAddresses = new HashSet<CustomerAddress>();

    public Customer() {
    }

    public Customer(Long id, String title, String firstname, String surname, String telephoneNumber) {
        this.id = id;
        this.title = title;
        this.firstname = firstname;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Set<CustomerAddress> getCustomerAddresses() {
        return new HashSet<CustomerAddress>(customerAddresses);
    }

    public void addAddressToCustomer(CustomerAddress address) {
        if (customerAddresses.contains(address)) {
            return;
        }
        customerAddresses.add(address);
        address.addCustomerToAddress(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getId().equals(customer.getId()) &&
                getTitle().equals(customer.getTitle()) &&
                getFirstname().equals(customer.getFirstname()) &&
                getSurname().equals(customer.getSurname()) &&
                Objects.equals(getTelephoneNumber(), customer.getTelephoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getFirstname(), getSurname(), getTelephoneNumber());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", customerAddresses='" + getAddressesToString() + '\'' +
                '}';
    }

    private String getAddressesToString() {
        return customerAddresses.stream()
                .map(CustomerAddress::toString).collect(joining(","));
    }
}
