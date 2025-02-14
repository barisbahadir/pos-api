package com.bahadir.pos.entity.permission;

import com.bahadir.pos.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
//@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {

    private String label;
    private String icon;

    @Enumerated(EnumType.STRING)
    private PermissionType type;

    private String route;
    private Integer orderValue;
    private String component;
    private String frameSrc;
    private boolean hide;
    private boolean hideTab;
    private boolean newFeature;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Permission parent;

    @OneToMany(mappedBy = "parent")
    private List<Permission> children;
}