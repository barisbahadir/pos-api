package com.bahadir.pos.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getJwtFromRequest(request);

        if(jwtToken != null && jwtTokenProvider.validateJwtToken(jwtToken)){
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }

        return null;
    }
}
