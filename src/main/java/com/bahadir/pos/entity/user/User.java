package com.bahadir.pos.entity.user;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role; // Temel rol (örn. Admin, Manager, User)

    @ManyToMany
    @JoinTable(name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions; // Detaylı izinler

    @Enumerated(EnumType.STRING)
    private AuthRole authRole;

//    @JsonIgnore  // Döngüsel referansı engelliyoruz
//    @OneToMany(mappedBy = "user")
//    private List<Product> products;

}
