package com.oss.ossv1.dto;

import com.oss.ossv1.data.entity.User;

/**
 * Represents the UserDTO class.
 */
public class UserDTO {

    private Integer id;
    private String username;
    private String email;

/**
 * UserDTO method.
 */
    public UserDTO() {
        // default constructor for JSON serialization
    }

/**
 * UserDTO method.
 */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Getters & Setters

/**
 * getId method.
 */
    public Integer getId() {
        return id;
    }

/**
 * setId method.
 */
    public void setId(Integer id) {
        this.id = id;
    }

/**
 * getUsername method.
 */
    public String getUsername() {
        return username;
    }

/**
 * setUsername method.
 */
    public void setUsername(String username) {
        this.username = username;
    }

/**
 * getEmail method.
 */
    public String getEmail() {
        return email;
    }

/**
 * setEmail method.
 */
    public void setEmail(String email) {
        this.email = email;
    }
}
