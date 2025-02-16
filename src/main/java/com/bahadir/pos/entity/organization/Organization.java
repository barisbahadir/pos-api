package com.bahadir.pos.entity.organization;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
//@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

    private Integer orderValue;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference // Company bilgisinin serileştirilmesini engelle
    private Company company;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference // Parent bilgisinin serileştirilmesini engelle
    private Organization parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference // Children listesinin serileştirilmesine izin ver
    private List<Organization> children;

    @OneToMany(mappedBy = "organization")
    @JsonManagedReference // Users listesinin serileştirilmesine izin ver
    private List<User> users;
}