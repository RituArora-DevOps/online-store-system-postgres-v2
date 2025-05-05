package com.oss.ossv1.data.entity;

import jakarta.persistence.*;  // Use Jakarta if using Spring Boot 3.x+, else javax.*

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Entity // Declares that this Java class is a JPA entity, meaning it maps to a table in your database.
@Table(name = "product", schema = "dbo")
@Inheritance(strategy = InheritanceType.JOINED) // This annotation tells JPA how to map inheritance between the base class and subclasses.
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "price", length = 50)
    private double price;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

}
