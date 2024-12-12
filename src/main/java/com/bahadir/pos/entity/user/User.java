package com.bahadir.pos.entity.user;

import com.bahadir.pos.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

//    @JsonIgnore  // Döngüsel referansı engelliyoruz
//    @OneToMany(mappedBy = "user")
//    private List<Product> products;

}
