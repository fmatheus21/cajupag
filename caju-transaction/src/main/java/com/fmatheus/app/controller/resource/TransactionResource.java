package com.fmatheus.app.controller.resource;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.facade.TransactionFacade;
import com.fmatheus.app.controller.security.authorize.AllAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionResource {

    private final TransactionFacade facade;

    @AllAuthorize
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public Map<String, String> performTransaction(@RequestBody @Valid TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        return this.facade.startTransaction(request, jwt);
    }
}
