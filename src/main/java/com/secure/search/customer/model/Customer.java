package com.secure.search.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private String address;
    private String pinCode;
    private int status;

    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns =@JoinColumn(name = "id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Customer(){

    }

    public Customer(int id, String firstName, String lastName, String email, String password, String mobileNumber, String address, String pinCode, int status, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.pinCode = pinCode;
        this.status = status;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
