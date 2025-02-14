package com.bahadir.pos.entity.company;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.organization.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
//@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @OneToMany(mappedBy = "company")
    private List<Organization> organizations;
}