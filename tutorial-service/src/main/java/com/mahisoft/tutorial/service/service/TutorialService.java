package com.mahisoft.tutorial.service.service;

import com.google.common.base.Strings;
import com.mahisoft.tutorial.service.controller.dto.*;
import com.mahisoft.tutorial.service.service.domain.ProductEntity;
import com.mahisoft.tutorial.service.service.repository.TutorialDummyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialDummyRepository repository;

    public String getGreeting(String person) {
        return String.format("Hello World %s!", person);
    }

    public Long createProduct(CreateProductRequest request) {
        return repository.create(
                ProductEntity
                        .builder()
                        .category(request.getCategory())
                        .discount(request.getDiscount())
                        .name(request.getName())
                        .price(request.getPrice())
                        .status(StatusType.Active)
                        .build()
        );
    }

    public ProductItem getProduct(Long id) {
        ProductEntity product = getProductEntity(id);
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

    private ProductEntity getProductEntity(Long id) {
        ProductEntity product = repository.get(id);
        if (product == null) {
            throw new NoSuchElementException(String.format("Product '%s' not found.", id));
        }
        return product;
    }

    public void updateProduct(Long id, UpdateProductRequest request) {
        ProductEntity product = getProductEntity(id);

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDiscount(request.getDiscount());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());

        repository.update(product);
    }

    public void partialUpdateProduct(Long id, PartialUpdateProductRequest request) {
        ProductEntity product = getProductEntity(id);

        if (!Strings.isNullOrEmpty(request.getName())) {
            product.setName(request.getName());
        }

        if (!Strings.isNullOrEmpty(request.getCategory())) {
            product.setCategory(request.getCategory());
        }

        if (request.getDiscount() != null) {
            product.setDiscount(request.getDiscount());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }

        repository.update(product);
    }

    public void deleteProduct(Long id) {
        ProductEntity product = getProductEntity(id);

        if (product.getStatus() == StatusType.Active) {
            throw new ValidationException(String.format("Product '%s' is active.", id));
        }

        repository.delete(id);
    }
}