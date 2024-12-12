package com.bahadir.pos.entity.category;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category extends BaseEntity {

    private Integer orderValue;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> products;
}
