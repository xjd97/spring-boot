package com.ejlchina.project.entity;

import java.io.Serializable;

public class IDName implements Serializable {

    private static final long serialVersionUID = 7960851297743995362L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
