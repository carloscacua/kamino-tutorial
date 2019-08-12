package com.mahisoft.tutorial.service.service.mapper;

import com.mahisoft.tutorial.service.controller.dto.ProductItem;
import com.mahisoft.tutorial.service.service.domain.ProductEntity;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductItem toDto(ProductEntity product){
        return ProductItem
                .builder()
                .category(product.getCategory())
                .name(product.getName())
                .discount(product.getDiscount())
                .price(product.getPrice())
                .id(product.getId())
                .status(product.getStatus())
                .build();
    }
}
