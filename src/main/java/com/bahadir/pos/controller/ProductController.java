package com.bahadir.pos.controller;

import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.Product;
import com.bahadir.pos.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Ürünleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Ürün oluştur
    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Ürün güncelle
    @PostMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/order-update")
    public ResponseEntity<String> updateOrderValues(@RequestBody List<OrderUpdateDto> orderedValues) {
        Boolean result = productService.updateOrderValues(orderedValues);
        String resultMsg = result
                ? "Urunler basariyla siralandi."
                : "Siralama kaydedilirken bir hata olustu, bos degerleri kontrol edins!";
        return ResponseEntity.ok(resultMsg);
    }

    // Ürün sil
    @PostMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.ok(true);
    }
}
