package com.fmatheus.app.controller.resource;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.facade.TransactionFacade;
import com.fmatheus.app.controller.security.authorize.AllAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionResource {

    private final TransactionFacade facade;

    @AllAuthorize
    @PostMapping
    public Map<String, String> performTransaction(@RequestBody @Valid TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        return this.facade.performTransaction(request, jwt);
    }
}
