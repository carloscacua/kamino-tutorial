package com.mahisoft.tutorial.service.service.repository;

import com.mahisoft.tutorial.service.service.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TutorialRepository extends JpaRepository<ProductEntity, Long> {

}
