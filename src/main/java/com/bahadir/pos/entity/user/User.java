package com.bahadir.pos.entity.user;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonBackReference // Organization bilgisinin serileştirilmesini engelle
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonManagedReference // Role bilgisinin serileştirilmesine izin ver
    private Role role;

//    @ManyToMany
//    @JoinTable(name = "user_permissions",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id"))
//    @JsonIgnore
//    private List<Permission> permissions; // Detaylı izinler

    @Enumerated(EnumType.STRING)
    private UserRole authRole;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "user_settings_id")
//    private UserSettings userSettings;

//    @JsonIgnore  // Döngüsel referansı engelliyoruz
//    @OneToMany(mappedBy = "user")
//    private List<Product> products;

}
