package com.fmatheus.app.controller.security;

import com.fmatheus.app.config.properties.JksProperties;
import com.fmatheus.app.config.properties.RegistredClientProperties;
import com.fmatheus.app.controller.util.CharacterUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

import static org.springframework.security.config.Customizer.withDefaults;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JksProperties jksProperties;
    private final RegistredClientProperties registredClientProperties;

    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverter(new CustomAuthenticationConverter())
                        .authenticationProvider(new CustomAuthenticationProvider(authorizationService(), tokenGenerator(), this.userDetailsService, this.passwordEncoder))
                        .accessTokenRequestConverters(getConverters())
                        .authenticationProviders(getProviders()))
                .oidc(withDefaults());

        http.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

        return http.build();


    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(withDefaults())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        CharSequence clientCredentialsSecret = new StringBuilder(this.registredClientProperties.getClientSecretOne());
        RegisteredClient registeredClientCredentials = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(this.registredClientProperties.getClientIdOne())
                .clientSecret(this.passwordEncoder.encode(clientCredentialsSecret))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(OidcScopes.OPENID)
                .scope("base")
                .scope("write")
                .build();

        CharSequence secret = new StringBuilder(this.registredClientProperties.getClientSecretTwo());
        RegisteredClient registeredPassword = RegisteredClient.withId("client")
                .clientId(this.registredClientProperties.getClientIdTwo())
                .clientSecret(this.passwordEncoder.encode(secret))
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("base")
                .scope("write")
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/myoauth2")
                .redirectUri("http://insomnia")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
                .authorizationGrantType(new AuthorizationGrantType(CustomOAuth2ParameterNames.CUSTOM_GRANT_TYPE))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofDays(1))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false) // True - Requer a confirmacao dos escopos.
                        .requireProofKey(false) // True - Requer  uma chave para cada solicitacao de autorizacao.
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClientCredentials, registeredPassword);
    }


    private Consumer<List<AuthenticationProvider>> getProviders() {
        return a -> a.forEach(provider -> log.info(provider.toString()));

    }

    private Consumer<List<AuthenticationConverter>> getConverters() {
        return a -> a.forEach(converter -> log.info(converter.toString()));
    }


    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator() {
        NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(tokenCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {

            OAuth2ClientAuthenticationToken principal = context.getPrincipal();

            if (principal.getDetails() instanceof CustomUser customUser) {

                CustomUserDetails customUserDetails = customUser.customUserDetails();

                if (context.getTokenType().getValue().equals("access_token")) {
                    context.getClaims()
                            .claim("account", customUserDetails.getAccount().getId())
                            .claim("card_number", Objects.requireNonNull(CharacterUtil.maskCardNumber(customUserDetails.getAccount().getCardNumber())));
                }

            }
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet());
    }

    @SneakyThrows
    @Bean
    public JWKSet jwkSet() {
        final String jksPath = this.jksProperties.getPath();
        final InputStream inputStream = new ClassPathResource(jksPath).getInputStream();
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());
        RSAKey rsaKey = RSAKey.load(keyStore, jksProperties.getAlias(), jksProperties.getKeypass().toCharArray());
        return new JWKSet(rsaKey);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }


    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("POST", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}