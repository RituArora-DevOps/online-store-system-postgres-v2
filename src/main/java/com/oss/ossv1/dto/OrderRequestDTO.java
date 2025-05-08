package com.oss.ossv1.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
/**
 * Represents the OrderRequestDTO class.
 */
public class OrderRequestDTO {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderItemDTO> items;

    @NotNull
    private PaymentRequestDTO payment;
}
