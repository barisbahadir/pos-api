package com.bahadir.pos.entity.role;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.permission.Permission;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
//@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    private String label;

    private Integer orderValue;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new LinkedHashSet<>();
}