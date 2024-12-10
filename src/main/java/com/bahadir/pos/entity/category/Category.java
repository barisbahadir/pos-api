package com.bahadir.pos.entity.category;

import com.bahadir.pos.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer orderValue;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> products;
}
