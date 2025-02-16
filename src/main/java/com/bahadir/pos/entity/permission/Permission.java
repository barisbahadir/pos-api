package com.bahadir.pos.entity.permission;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Set;

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
    @JsonBackReference // Parent bilgisinin serileştirilmesini engelle
    private Permission parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference // Children listesinin serileştirilmesine izin ver
    private List<Permission> children;

    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference // Roles bilgisinin serileştirilmesini engelle
    private Set<Role> roles;

    // Transient alan ile parentId'yi JSON'a ekleme
    @Transient
    @JsonProperty("parentId")
    private String parentId;

    // Parent set edildiğinde parentId de güncellensin
    public void setParent(Permission parent) {
        this.parent = parent;
        this.parentId = (parent != null) ? parent.getId().toString() : null;
    }

    // JSON içinde parentId'nin görünmesini sağlayan getter
    public String getParentId() {
        if (this.parent != null && Hibernate.isInitialized(this.parent)) {
            return this.parent.getId().toString();
        }
        return null;
    }

}