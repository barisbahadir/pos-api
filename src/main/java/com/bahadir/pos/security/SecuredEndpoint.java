package com.bahadir.pos.security;

import com.bahadir.pos.entity.user.AuthRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecuredEndpoint {

    AuthRole role() default AuthRole.USER; // Varsayılan rol

    boolean filter() default false; // Filtreleme seçeneği
}