package com.fmatheus.app.controller.security;


import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.*;

@Getter
public class CustomAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;
    private final Set<String> scopes;

    public CustomAuthenticationToken(Authentication clientPrincipal, @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(CustomOAuth2ParameterNames.CUSTOM_GRANT_TYPE), clientPrincipal, additionalParameters);
        this.username = (String) Objects.requireNonNull(additionalParameters).get("card_number");
        this.password = (String) Objects.requireNonNull(additionalParameters).get("password");
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomAuthenticationToken that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername());
    }

}
