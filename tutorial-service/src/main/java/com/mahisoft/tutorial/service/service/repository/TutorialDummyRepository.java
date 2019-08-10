package com.mahisoft.tutorial.service.service.repository;

import com.mahisoft.tutorial.service.service.domain.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TutorialDummyRepository {

    private final static AtomicLong ID = new AtomicLong();

    private final static Map<Long, ProductEntity> PRODUCTS = new HashMap<>();

    public Long create(ProductEntity product) {
        Long id = ID.incrementAndGet();
        product.setId(id);
        PRODUCTS.put(id, product);
        return id;
    }

    public void update(ProductEntity product) {
        PRODUCTS.put(product.getId(), product);
    }

    public void delete(Long id) {
        PRODUCTS.remove(id);
    }

    public ProductEntity get(Long id) {
        return PRODUCTS.get(id);
    }

    public List<ProductEntity> get() {
        return new ArrayList<>(PRODUCTS.values());
    }

}