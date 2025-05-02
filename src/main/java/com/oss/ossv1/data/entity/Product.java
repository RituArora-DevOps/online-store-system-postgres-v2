package com.oss.ossv1.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "product", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 5)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "price", length = 50)
    private double price;
}
