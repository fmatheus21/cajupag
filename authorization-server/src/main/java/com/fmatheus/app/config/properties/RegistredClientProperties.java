package com.fmatheus.app.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "authorization-server.security.registred-client")
public class RegistredClientProperties {
    private String clientIdOne;
    private String clientSecretOne;
    private String clientIdTwo;
    private String clientSecretTwo;
}

