package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store past orders
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_history_id")
    private List<Order> orders;
}
