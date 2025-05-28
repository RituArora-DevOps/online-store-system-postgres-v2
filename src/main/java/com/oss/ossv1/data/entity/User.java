package com.oss.ossv1.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // matches your table name exactly
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Represents the User class.
 */
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "is_admin", nullable = false, columnDefinition = "TINYINT(1)")
    @JsonProperty("isAdmin")
    private boolean isAdmin = false;

    /**
     * Constructor with admin flag
     */
    public User(String username, String email, String passwordHash, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    /**
     * Check if user is an administrator
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Set admin status
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
