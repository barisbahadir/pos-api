package com.bahadir.pos.entity.company;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.organization.Organization;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
//@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    private Integer orderValue;

    @OneToMany(mappedBy = "company")
    private List<Organization> organizations;
}