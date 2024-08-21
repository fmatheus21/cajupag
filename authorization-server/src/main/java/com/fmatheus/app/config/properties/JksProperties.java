package com.fmatheus.app.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "authorization-server.security.jks")
public class JksProperties {
    private String keypass;
    private String storepass;
    private String alias;
    private String path;
}
