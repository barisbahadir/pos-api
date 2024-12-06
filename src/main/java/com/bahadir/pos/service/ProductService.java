package com.bahadir.pos.service;

import com.bahadir.pos.entity.Product;
import com.bahadir.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Ürünleri listele
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Yeni ürün oluştur
    public Product createProduct(Product product) {
        product.setOrderValue(1);
        return productRepository.save(product);
    }

    // Ürün güncelle
    public Product updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            // Güncelleme işlemini gerçekleştirebiliriz, örneğin:
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setCategory(updatedProduct.getCategory());
            // Diğer gerekli alanları da güncelleyebilirsiniz.

            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("Ürün bulunamadı: " + productId);
        }
    }

    // Ürün sil
    public void deleteProduct(Long productId) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new IllegalArgumentException("Ürün bulunamadı: " + productId);
        }
    }

    // Tüm ürünleri sil
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
