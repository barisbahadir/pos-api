package com.bahadir.pos.controller;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.OrderUpdateItemDto;
import com.bahadir.pos.entity.Product;
import com.bahadir.pos.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Category> sortedCategories = categories.stream()
                .sorted(Comparator
                        .comparingInt(Category::getOrderValue) // 1. Kriter: orderValue
                        .thenComparingInt(category -> category.getProducts().size() * -1)) // 2. Kriter: product boyutu (azalan)
                .peek(category -> {
                    // Her kategori içindeki ürünleri sıralama
                    List<Product> sortedProducts = category.getProducts().stream()
                            .sorted(Comparator.comparingInt(Product::getOrderValue)) // Ürünleri orderValue'ya göre sırala
                            .collect(Collectors.toList());
                    category.setProducts(sortedProducts); // Sıralanmış ürünleri ayarla
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(sortedCategories);
    }

    // Yeni kategori oluştur
    @PostMapping("/add")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    @PostMapping("/order-update")
    public ResponseEntity<String> updateOrderValues(@RequestBody OrderUpdateDto orderData) {
        Boolean result = categoryService.updateOrderValues(orderData.getOrderedValues());
        String resultMsg = result
                ? "Kategoriler basariyla siralandi."
                : "Siralama kaydedilirken bir hata olustu, bos degerleri kontrol edins!";
        return ResponseEntity.ok(resultMsg);
    }

    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.ok(true);
    }
}
