package com.secure.search.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description_category;


}
