package com.bahadir.pos.controller;

import com.bahadir.pos.entity.category.Category;
import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.product.Product;
import com.bahadir.pos.entity.user.AuthRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<List<Category>> listAll() {
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

    @GetMapping("/list/{categoryId}")
    public ResponseEntity<Category> listById(@PathVariable Long categoryId) {
        Optional<Category> categoryResult = categoryService.getCategoryById(categoryId);
        if(!categoryResult.isPresent()){
            throw new ApiException("Kategori bulunamadi!");
        }
        return ResponseEntity.ok(categoryResult.get());
    }

    // Yeni kategori oluştur
    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    // Ürün güncelle
    @PostMapping("/update/{categoryId}")
    public ResponseEntity<Category> update(@PathVariable Long categoryId, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @PostMapping("/order-update")
    public ResponseEntity<String> updateOrderValues(@RequestBody OrderUpdateDto orderData) {
        Boolean result = categoryService.updateOrderValues(orderData.getOrderedValues());
        String resultMsg = result
                ? "Kategoriler basariyla siralandi."
                : "Siralama kaydedilirken bir hata olustu, bos degerleri kontrol edins!";
        return ResponseEntity.ok(resultMsg);
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(true);
    }

    @SecuredEndpoint(role = AuthRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        categoryService.deleteAllCategories();
        return ResponseEntity.ok(true);
    }
}
