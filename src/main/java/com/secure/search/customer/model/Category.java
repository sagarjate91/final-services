package com.secure.search.customer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description_category;

    public Category(){

    }

    public Category(int id, String name, String description_category) {
        this.id = id;
        this.name = name;
        this.description_category = description_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription_category() {
        return description_category;
    }

    public void setDescription_category(String description_category) {
        this.description_category = description_category;
    }
}
