package com.ecommerce.dto.request;

import com.ecommerce.model.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters.")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    @NotBlank(message = "SKU is required.")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters.")
    private String sku;

    @NotNull(message = "Product price is required.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Product price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "Discount is required.")
    @DecimalMin(value = "0.0",
            message = "Discount cannot be negative.")
    private BigDecimal discount;

    @NotNull(message = "Category ID is required.")
    @Positive(message = "Category ID must be greater than 0.")
    private Long categoryId;

    @NotNull(message = "Brand ID is required.")
    @Positive(message = "Brand ID must be greater than 0.")
    private Long brandId;

    private ProductStatus status;
}