package com.bahadir.pos.controller;

import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.product.Product;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.ProductService;
import com.bahadir.pos.utils.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/list/{id}")
    public ResponseEntity<Product> listById(@PathVariable String id) {
        Optional<Product> productResult = productService.getProductById(ApiUtils.getPathId(id));
        if (!productResult.isPresent()) {
            throw new ApiException("Urun bulunamadi!");
        }
        return ResponseEntity.ok(productResult.get());
    }

    // Ürün oluştur
    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Ürün güncelle
    @PostMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(ApiUtils.getPathId(id), product);
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
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        productService.deleteProduct(ApiUtils.getPathId(id));
        return ResponseEntity.ok(true);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        productService.deleteAllProducts();
        return ResponseEntity.ok(true);
    }
}
