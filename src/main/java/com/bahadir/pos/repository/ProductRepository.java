package com.bahadir.pos.repository;

import com.bahadir.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    void deleteByCategoryId(Long categoryId);
    
}