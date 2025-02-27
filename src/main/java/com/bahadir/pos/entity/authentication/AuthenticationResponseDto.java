package com.bahadir.pos.entity.authentication;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    private Long id;
    private String email;
    private String username;
    private String name;
    private UserRole role;
    private String avatar;
    private String status;
    private List<Permission> permissions;
    private String token;

    @Builder.Default
    private Boolean isAuthenticated = false;

    private AuthenticationType authType;
    private String twoFactorQrCode;

}
