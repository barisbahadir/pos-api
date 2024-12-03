package com.bahadir.pos.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Ürün adı
    private String barcode;
    private BigDecimal price;  // Ürün fiyatı
    private String image;  // Ürün resmi (base64 string)
    private Integer stockQuantity;  // Ürün stoğu

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference  // Kategorinin geri referansı (kategorilerle ilgili gereksiz döngüyü önler)
    private Category category;  // Ürünün kategorisi
}
