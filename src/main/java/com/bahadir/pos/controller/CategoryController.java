package com.bahadir.pos.controller;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Kategorileri listele
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Yeni kategori oluştur
    @PostMapping("/add")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.ok(true);
    }
}
