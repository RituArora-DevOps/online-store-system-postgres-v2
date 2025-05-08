package com.oss.ossv1.dto;
import lombok.Data;
import jakarta.validation.constraints.*;
@Data
/**
 * Represents the OrderItemDTO class.
 */
public class OrderItemDTO {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    // Getters and Setters
}
