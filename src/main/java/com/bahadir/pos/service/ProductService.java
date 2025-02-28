package com.bahadir.pos.service;

import com.bahadir.pos.entity.BaseStatus;
import com.bahadir.pos.entity.OrderUpdateItemDto;
import com.bahadir.pos.entity.product.Product;
import com.bahadir.pos.repository.ProductRepository;
import com.bahadir.pos.utils.ApiUtils;
import com.bahadir.pos.utils.ImageUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    // Ürünleri listele
//    @Cacheable(value = "products", key = "'all_products'")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //    @Cacheable(value = "products", key = "#productId")
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // Yeni ürün oluştur
//    @CachePut(value = "products", key = "#product.id")
    public Product createProduct(Product product) {
        product.setOrderValue(1);

        if (product.getBarcode() == null || product.getBarcode().isEmpty())
            product.setBarcode(ApiUtils.generateBarcode());

        if (product.getImage() != null && !product.getImage().isEmpty())
            product.setImage(ImageUtils.compressImage(product.getImage()));

        product.setStatus(BaseStatus.ENABLE);
        return productRepository.save(product);
    }

    // Ürün güncelle
//    @CachePut(value = "products", key = "#updatedProduct.id")
    public Product updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCategory(updatedProduct.getCategory());

            existingProduct.setPurchasePrice(updatedProduct.getPurchasePrice());
            existingProduct.setProfitMargin(updatedProduct.getProfitMargin());
            existingProduct.setTaxRate(updatedProduct.getTaxRate());
            existingProduct.setPrice(updatedProduct.getPrice());

            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setBarcode(updatedProduct.getBarcode());
            existingProduct.setImage(updatedProduct.getImage());
            existingProduct.setStatus(updatedProduct.getStatus() != null ? updatedProduct.getStatus() : BaseStatus.ENABLE);

            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("Ürün bulunamadı: " + productId);
        }
    }

    @Transactional
    public Boolean updateOrderValues(Long categoryId, List<OrderUpdateItemDto> updates) {

        Boolean hasNullValues = updates.stream()
                .anyMatch(update -> update.getId() == null || update.getOrderValue() == null);

        if (hasNullValues || categoryId == null || updates == null || updates.isEmpty()) {
            return false;
        } else {
            StringBuilder query = new StringBuilder("UPDATE product SET order_value = CASE ");
            List<Long> ids = new ArrayList<>();

            for (OrderUpdateItemDto update : updates) {
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
//    @CacheEvict(value = "products", key = "#productId")
    public void deleteProduct(Long productId) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new IllegalArgumentException("Ürün bulunamadı: " + productId);
        }
    }

    // Tüm ürünleri sil
//    @CacheEvict(value = "products", allEntries = true)
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
