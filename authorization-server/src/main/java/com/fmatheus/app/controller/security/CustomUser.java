package com.fmatheus.app.controller.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record CustomUser(CustomUserDetails customUserDetails, Collection<GrantedAuthority> authorities) {
}
