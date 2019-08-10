package com.mahisoft.tutorial.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItem {

    private Long id;

    private String name;

    private BigDecimal price;

    private String category;

    private Double discount;

    private StatusType status;
}