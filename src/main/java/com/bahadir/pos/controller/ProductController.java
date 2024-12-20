package com.bahadir.pos.controller;

import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.product.Product;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Ürünleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Product>> listAll() {
        List<Product> products = productService.getAllProducts();

        List<Product> sortedCategories = products.stream()
                .sorted(Comparator.comparingInt(Product::getOrderValue))
                .collect(Collectors.toList());

        return ResponseEntity.ok(sortedCategories);
    }

    // Ürün oluştur
    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Ürün güncelle
    @PostMapping("/update/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/order-update")
    public ResponseEntity<String> updateOrderValues(@RequestBody OrderUpdateDto orderData) {
        Boolean result = productService.updateOrderValues(orderData.getCategoryId(), orderData.getOrderedValues());
        String resultMsg = result
                ? "Urunler basariyla siralandi."
                : "Siralama kaydedilirken bir hata olustu, bos degerleri kontrol edins!";
        return ResponseEntity.ok(resultMsg);
    }

    // Ürün sil
    @PostMapping("/delete/{productId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(true);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        productService.deleteAllProducts();
        return ResponseEntity.ok(true);
    }
}
