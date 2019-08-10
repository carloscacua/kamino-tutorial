package com.mahisoft.tutorial.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartialUpdateProductRequest {

    @Size(min = 3, max = 25, message = "Name must have between 3 and 25 characters")
    private String name;

    private BigDecimal price;

    @Size(min = 3, max = 25, message = "Category must have between 3 and 25 characters")
    private String category;

    @Min(value = 10, message = "Discount min value is 10")
    @Max(value = 90, message = "Discount max value is 90")
    private Double discount;

    private StatusType status;

}