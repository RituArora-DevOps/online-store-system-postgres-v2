package com.oss.ossv1.dto;

import com.oss.ossv1.data.entity.User;

public class UserDTO {

    private Integer id;
    private String username;
    private String email;

    public UserDTO() {
        // default constructor for JSON serialization
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
