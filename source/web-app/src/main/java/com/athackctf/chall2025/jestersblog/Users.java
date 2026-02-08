package com.athackctf.chall2025.jestersblog;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class Users {

    @Id
    private int idusers;
    private String username;

    private String password;

    public int getId() {
        return idusers;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
