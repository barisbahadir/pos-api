package com.bahadir.pos.entity.user;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.authentication.AuthenticationType;
import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonBackReference // Organization bilgisinin serileştirilmesini engelle
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonManagedReference // Role bilgisinin serileştirilmesine izin ver
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserRole authRole;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AuthenticationType authType = AuthenticationType.NONE;

    @Column(columnDefinition = "TEXT")
    private String twoFactorAuthSecretKey;

    private String twoFactorEmailCode;

}
