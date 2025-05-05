package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "grocery")
@PrimaryKeyJoinColumn(name = "product_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grocery extends Product {

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
