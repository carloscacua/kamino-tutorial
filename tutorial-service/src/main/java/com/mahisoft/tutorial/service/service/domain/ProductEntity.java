package com.mahisoft.tutorial.service.service.domain;

import com.mahisoft.tutorial.service.controller.dto.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    private Long id;

    private String name;

    private BigDecimal price;

    private String category;

    private Double discount;

    private StatusType status;
}