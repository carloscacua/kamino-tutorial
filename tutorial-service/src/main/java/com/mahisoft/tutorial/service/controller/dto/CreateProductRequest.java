package com.mahisoft.tutorial.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 25, message = "Name must have between 3 and 25 characters")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Category is required")
    @Size(min = 3, max = 25, message = "Category must have between 3 and 25 characters")
    private String category;

    @Min(value = 10, message = "Discount min value is 10")
    @Max(value = 90, message = "Discount max value is 90")
    private Double discount;

    @SuppressWarnings("unused")
    @AssertTrue(message = "Discount is required when the category is Clearance")
    private boolean isDiscountValid() {
        return !("Clearance".equals(this.category) && discount == null);
    }

}