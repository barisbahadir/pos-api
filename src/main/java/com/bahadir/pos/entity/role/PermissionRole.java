package com.bahadir.pos.entity.role;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.permission.Permission;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
//@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRole extends BaseEntity {

    private String label;
    private String status;
    private Integer orderValue;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;
}