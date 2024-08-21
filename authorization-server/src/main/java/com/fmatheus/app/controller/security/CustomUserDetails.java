package com.fmatheus.app.controller.security;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
public class CustomUserDetails extends User {

    private final com.fmatheus.app.model.entity.Account account;

    public CustomUserDetails(com.fmatheus.app.model.entity.Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getCardNumber(), account.getPassword(), authorities);
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account);
    }
}
