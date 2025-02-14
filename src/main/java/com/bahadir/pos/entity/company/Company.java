package com.bahadir.pos.entity.company;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.organization.Organization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
//@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    private Integer orderValue;

    @OneToMany(mappedBy = "company")
    private List<Organization> organizations;
}