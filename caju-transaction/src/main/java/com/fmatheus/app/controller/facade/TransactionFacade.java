package com.fmatheus.app.controller.facade;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.util.AppUtil;
import com.fmatheus.app.model.entity.Account;
import com.fmatheus.app.model.entity.AccountBalanceJoin;
import com.fmatheus.app.model.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionFacade {

    private final AccountService accountService;

    public Map<String, String> performTransaction(TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        var claim = jwt.getClaims().get("account").toString();
        UUID account = UUID.fromString(claim);

        Account result = this.accountService.findByAccountPessimisticWriteLock(account).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Collection<AccountBalanceJoin> collection = this.updateAccountBalanceJoin(request, result.getAccountBalanceJoinCollection());
        result.getAccountBalanceJoinCollection().clear();
        result.getAccountBalanceJoinCollection().addAll(collection);

        this.accountService.save(result);

        return null;
    }

    private Collection<AccountBalanceJoin> updateAccountBalanceJoin(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        return collection.stream()
                .map(accountBalance -> {
                    if (accountBalance.getBalance().getName().equalsIgnoreCase(AppUtil.determineBalanceType(request.getMcc()))) {
                        return AccountBalanceJoin.builder()
                                .pk(accountBalance.getPk())
                                .currentBalance(BigDecimal.ZERO)
                                .totalBalance(accountBalance.getTotalBalance())
                                .account(accountBalance.getAccount())
                                .balance(accountBalance.getBalance())
                                .build();
                    }
                    return accountBalance;
                })
                .toList();
    }

}
