package com.fmatheus.app.controller.security;


import com.fmatheus.app.controller.util.CharacterUtil;
import com.fmatheus.app.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    /**
     * @param username nome de usuario
     * @return UserDetails
     * @throws UsernameNotFoundException Lancamento de excecao
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var cardNumber = CharacterUtil.maskCardNumber(username);

        log.info("Procurando pelo cartao {}", cardNumber);
        var account = this.accountService.findByCardNumber(username).orElseThrow(() -> new OAuth2AuthenticationException(OAuthUtil.authentiucationError()));

        log.info("Cartao encontrado: {}", cardNumber);

        return new CustomUserDetails(account, this.authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("public_permission"));
        return simpleGrantedAuthorities;
    }

}
