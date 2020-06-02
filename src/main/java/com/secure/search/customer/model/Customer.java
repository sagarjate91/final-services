package com.secure.search.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private String address;
    private String pinCode;
    private int status=0;

    @JsonIgnore
    @OneToOne(mappedBy ="Customer" ,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private Cart cart;

    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns =@JoinColumn(name = "id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


}
