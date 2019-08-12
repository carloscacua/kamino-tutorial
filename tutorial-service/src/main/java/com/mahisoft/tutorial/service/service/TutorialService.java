package com.mahisoft.tutorial.service.service;

import com.google.common.base.Strings;
import com.mahisoft.tutorial.service.controller.dto.*;
import com.mahisoft.tutorial.service.service.domain.ProductEntity;
import com.mahisoft.tutorial.service.service.mapper.ProductMapper;
import com.mahisoft.tutorial.service.service.repository.TutorialDummyRepository;
import com.mahisoft.tutorial.service.service.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorialService {

    //private final TutorialDummyRepository repository;
    private final TutorialRepository realRepository;

    public String getGreeting(String person) {
        return String.format("Hello World %s!", person);
    }


    @Transactional(rollbackOn = Exception.class)
    public Long createProduct(CreateProductRequest request) {
        ProductEntity entity = ProductEntity
                .builder()
                .category(request.getCategory())
                .discount(request.getDiscount())
                .name(request.getName())
                .price(request.getPrice())
                .status(StatusType.Active)
                .build();
        ProductEntity savedRecord = realRepository.save(entity);
        return savedRecord.getId();
    }

    public ProductItem getProduct(Long id) {
        return ProductMapper.toDto(getProductEntity(id));
    }

    public List<ProductItem> getProducts()
    {
        List<ProductEntity> list =  realRepository.findAll();
        return list.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    private ProductEntity getProductEntity(Long id) {
        ProductEntity product = realRepository.findOne(id);
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

        realRepository.save(product);
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

        realRepository.save(product);
    }

    public void deleteProduct(Long id) {
        ProductEntity product = getProductEntity(id);

        if (product.getStatus() == StatusType.Active) {
            throw new ValidationException(String.format("Product '%s' is active.", id));
        }

        realRepository.delete(id);
    }
}