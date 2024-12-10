package com.bahadir.pos.entity.product;

import com.bahadir.pos.entity.category.Category;
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

    private String name;
    private String barcode;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer orderValue;

    // BYTEA ile görüntü verisini saklıyoruz
    @Column(name = "image", columnDefinition = "BYTEA")
    private byte[] image;

    @Transient
    private String imageBase64;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference  // Kategorinin geri referansı (kategorilerle ilgili gereksiz döngüyü önler)
    private Category category;  // Ürünün kategorisi
}
