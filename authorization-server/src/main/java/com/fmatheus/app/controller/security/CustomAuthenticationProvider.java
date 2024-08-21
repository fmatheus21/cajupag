package com.fmatheus.app.controller.security;


import com.fmatheus.app.controller.util.CharacterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String username;
    private String password;
    private final OAuth2AuthorizationService authorizationService;
    private final UserDetailsService userDetailsService;
    private OAuth2Authorization.Builder authorizationBuilder;
    private DefaultOAuth2TokenContext.Builder tokenContextBuilder;
    private OAuth2TokenContext tokenContext;
    private OAuth2AccessToken accessToken;
    private RegisteredClient registeredClient;
    private OAuth2ClientAuthenticationToken clientPrincipal;
    private CustomAuthenticationToken customAuthenticationToken;
    private OAuth2RefreshToken refreshToken;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final Set<String> authorizedScopes = new HashSet<>();
    private CustomUserDetails customUserDetails;
    private final PasswordEncoder passwordEncoder;


    public CustomAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                        UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        Assert.notNull(authorizationService, "authorizationService nao pode ser nulo");
        Assert.notNull(tokenGenerator, "TokenGenerator nao pode ser nulo");
        Assert.notNull(userDetailsService, "UserDetailsService nao pode ser nulo");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        this.customAuthenticationToken = (CustomAuthenticationToken) authentication;
        this.clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(this.customAuthenticationToken);
        this.registeredClient = this.clientPrincipal.getRegisteredClient();
        this.username = CharacterUtil.removeSpecialCharacters(this.customAuthenticationToken.getUsername());
        this.password = this.customAuthenticationToken.getPassword();
        User user = this.validateUser();
        this.authorizedScopes.addAll(registeredClient.getScopes());
        this.generateNewContextHolder(user);
        this.defaultOAuth2TokenContext();
        this.authorizationBuilder();
        this.generateAccessToken();
        this.generateRefreshToken();
        OAuth2Authorization authorization = this.authorizationBuilder.build();
        this.authorizationService.save(authorization);
        return new OAuth2AccessTokenAuthenticationToken(this.registeredClient, this.clientPrincipal, this.accessToken, this.refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }


    private User validateUser() {

        try {
            this.customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(this.username);
        } catch (UsernameNotFoundException e) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
        }

        var cardNumber = CharacterUtil.maskCardNumber(this.username);

        log.info("Comparando senha do cartao: {}", cardNumber);
        CharSequence charPassword = new StringBuilder(this.password);
        if (!this.passwordEncoder.matches(charPassword, this.customUserDetails.getPassword()) || !Objects.requireNonNull(CharacterUtil.removeSpecialCharacters(this.customUserDetails.getUsername())).equals(this.username)) {
            log.info("Número do Cartão ou senha incorreto.");
            throw new OAuth2AuthenticationException(OAuthUtil.authentiucationError());
        }
        log.info("Senha do cartao {} confirmada.", cardNumber);

        return this.customUserDetails;
    }

    private void defaultOAuth2TokenContext() {
        this.tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(this.registeredClient)
                .principal(this.clientPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(this.authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType(CustomOAuth2ParameterNames.CUSTOM_GRANT_TYPE))
                .authorizationGrant(this.customAuthenticationToken);
    }

    private void authorizationBuilder() {
        this.authorizationBuilder = OAuth2Authorization.withRegisteredClient(this.registeredClient)
                .attribute(Principal.class.getName(), this.clientPrincipal)
                .principalName(this.clientPrincipal.getName())
                .authorizationGrantType(new AuthorizationGrantType(CustomOAuth2ParameterNames.CUSTOM_GRANT_TYPE))
                .authorizedScopes(this.authorizedScopes);
    }


    /**
     * Cria um novo contexto de seguranca.
     *
     * @param user User
     */
    private void generateNewContextHolder(User user) {
        log.info("Gerando novo Context Holder.");
        var oAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var customUser = new CustomUser(this.customUserDetails, user.getAuthorities());
        oAuth2ClientAuthenticationToken.setDetails(customUser);

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(oAuth2ClientAuthenticationToken);
        SecurityContextHolder.setContext(context);
        log.info("Context Holder gerado.");
    }


    /**
     * Gerar access token.
     */
    private void generateAccessToken() {
        log.info("Gerando Access Token.");
        this.tokenContext = this.tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();

        var generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            throw new OAuth2AuthenticationException(OAuthUtil.tokenError());
        }

        this.accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(), generatedAccessToken.getExpiresAt(), this.tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
            this.authorizationBuilder.token(this.accessToken, metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, (claimAccessor).getClaims()));
        } else {
            this.authorizationBuilder.accessToken(this.accessToken);
        }

        log.info("Access Token gerado.");
    }

    /**
     * Gerar refresh token.
     */
    private void generateRefreshToken() {
        log.info("Gerando Refresh Token.");
        if (Objects.requireNonNull(this.registeredClient).getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !this.clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            this.tokenContext = this.tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            var generatedRefreshToken = this.tokenGenerator.generate(this.tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                throw new OAuth2AuthenticationException(OAuthUtil.tokenError());
            }
            this.refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            this.authorizationBuilder.refreshToken(this.refreshToken);
            log.info("Refresh Token gerado.");

        }
    }


}

