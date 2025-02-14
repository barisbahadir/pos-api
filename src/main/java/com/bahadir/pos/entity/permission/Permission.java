package com.bahadir.pos.entity.permission;

import com.bahadir.pos.entity.BaseEntity;
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
    @JsonBackReference
    private Permission parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Permission> children;

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