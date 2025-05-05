package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clothing")
@PrimaryKeyJoinColumn(name = "product_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Clothing extends Product {

    @Column(name = "size", length = 10)
    private String size;

    @Column(name = "color", length = 20)
    private String color;
}
