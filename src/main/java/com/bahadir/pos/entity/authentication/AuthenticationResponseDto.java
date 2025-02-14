package com.bahadir.pos.entity.authentication;

import com.bahadir.pos.entity.user.AuthRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    private String email;
    private AuthRole role;
    private String token;
}
