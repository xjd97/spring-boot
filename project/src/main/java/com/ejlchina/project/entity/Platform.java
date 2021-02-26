package com.ejlchina.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Platform implements Serializable {
    private static final long serialVersionUID = 8118698517587251775L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "phone")
    private String phone;
    @Column(name = "company")
    private String company;
    @Column(name = "name")
    private String name;

    public Platform() {}

    public Platform(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
