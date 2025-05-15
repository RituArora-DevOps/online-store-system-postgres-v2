package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users") // matches your table name exactly
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
}
