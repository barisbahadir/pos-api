package com.bahadir.pos.repository;

import com.bahadir.pos.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
