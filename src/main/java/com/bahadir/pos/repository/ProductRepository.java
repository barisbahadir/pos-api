package com.bahadir.pos.repository;

import com.bahadir.pos.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    void deleteByCategoryId(Long categoryId);
    
}