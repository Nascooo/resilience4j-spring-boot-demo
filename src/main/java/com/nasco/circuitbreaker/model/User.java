package com.nasco.circuitbreaker.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User extends Response implements Serializable {

    private int id;
    private String name;
    private String position;

    public User() {
    }

    public User(int id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
