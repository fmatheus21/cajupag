package com.fmatheus.app.controller.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public class OAuthUtil {

    private static final String INVALID_REQUEST = "O parâmetro [%s] não foi encontrado.";

    private OAuthUtil() {
        throw new IllegalStateException(this.getClass().getName());
    }

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    public static OAuth2Error tokenError() {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "O gerador de token falhou ao gerar o token de acesso.", ERROR_URI);
    }

    public static OAuth2Error grantTypeInvalidError() {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "grantType inválido.", ERROR_URI);
    }

    public static OAuth2Error grantTypeError() {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "grantType inválido.", ERROR_URI);
    }


    public static OAuth2Error cardNumberError() {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, String.format(INVALID_REQUEST, OAuth2ParameterNames.USERNAME), ERROR_URI);
    }

    public static OAuth2Error passwordError() {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, String.format(INVALID_REQUEST, OAuth2ParameterNames.PASSWORD), ERROR_URI);
    }

    public static OAuth2Error authentiucationError() {
        return new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, "Número do Cartão ou senha incorreto.", ERROR_URI);
    }


}
