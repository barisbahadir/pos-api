package com.bahadir.pos.service;

import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.entity.Product;
import com.bahadir.pos.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
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

    @Transactional
    public Boolean updateOrderValues(Long categoryId, List<OrderUpdateDto> updates) {

        Boolean hasNullValues = updates.stream()
                .anyMatch(update -> update.getId() == null || update.getOrderValue() == null);

        if (hasNullValues || categoryId == null || updates == null || updates.isEmpty()) {
            return false;
        } else {
            StringBuilder query = new StringBuilder("UPDATE product SET order_value = CASE ");
            List<Long> ids = new ArrayList<>();

            for (OrderUpdateDto update : updates) {
                query.append("WHEN id = ").append(update.getId()).append(" THEN ").append(update.getOrderValue()).append(" ");
                ids.add(update.getId());
            }

            // Verilmeyen id'ler için order_value'yu 1 olarak ayarlama
            query.append("ELSE 1 ");
            query.append("END WHERE category_id = :categoryId");

            // Sorguyu çalıştır
            Query nativeQuery = entityManager.createNativeQuery(query.toString());
            nativeQuery.setParameter("categoryId", categoryId);
            nativeQuery.executeUpdate();

            return true;
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
