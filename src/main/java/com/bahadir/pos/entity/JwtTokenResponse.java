package com.bahadir.pos.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
    private String jwtToken;
    private String sessionId;
}
