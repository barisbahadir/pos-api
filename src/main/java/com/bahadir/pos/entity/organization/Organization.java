package com.bahadir.pos.entity.organization;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
//@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

    private Integer orderValue;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @OneToMany(mappedBy = "parent")
    private List<Organization> children;

    @OneToMany(mappedBy = "organization")
    private List<User> users;
}