package com.pbl.nagadapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "api_users")
public class ApiUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column
    private String password;

    public  ApiUsers(){}
    public ApiUsers(String username, String password){
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
