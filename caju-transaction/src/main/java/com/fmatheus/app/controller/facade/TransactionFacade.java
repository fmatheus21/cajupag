package com.fmatheus.app.controller.facade;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.util.AppUtil;
import com.fmatheus.app.model.entity.*;
import com.fmatheus.app.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionFacade {

    private final AccountService accountService;
    private Balance balance;

    public Map<String, String> performTransaction(TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        var claim = jwt.getClaims().get("account").toString();
        UUID account = UUID.fromString(claim);

        Account result = this.accountService.findByAccountPessimisticWriteLock(account).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Map<String, String> map = new HashMap<>();

        if (!this.validateAccountBalance(request, result.getAccountBalanceJoinCollection())) {
            map.put("code", "51");
            return map;
        }

        Collection<AccountBalanceJoin> collection = this.updateAccountBalanceJoin(request, result.getAccountBalanceJoinCollection());
        result.getAccountBalanceJoinCollection().clear();
        result.getAccountBalanceJoinCollection().addAll(collection);

        Transactions transactions = Transactions.builder()
                .account(result)
                .amount(request.getTotalAmount())
                .status("APROVADA")
                .balance(this.balance)
                .establishment(Establishment.builder()
                        .id(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        result.getTransactionCollection().add(transactions);

        this.accountService.save(result);

        map.put("code", "00");

        return map;
    }

    private Collection<AccountBalanceJoin> updateAccountBalanceJoin(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        return collection.stream()
                .map(accountBalance -> {
                    if (accountBalance.getBalance().getName().equalsIgnoreCase(AppUtil.determineBalanceType(request.getMcc()))) {
                        this.balance = accountBalance.getBalance();
                        return AccountBalanceJoin.builder()
                                .pk(accountBalance.getPk())
                                .currentBalance(accountBalance.getCurrentBalance().subtract(request.getTotalAmount()))
                                .totalBalance(accountBalance.getTotalBalance())
                                .account(accountBalance.getAccount())
                                .balance(accountBalance.getBalance())
                                .build();
                    }
                    return accountBalance;
                })
                .toList();
    }


    private boolean validateAccountBalance(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        var resultMcc = AppUtil.determineBalanceType(request.getMcc());
        Optional<AccountBalanceJoin> optional = collection.stream().filter(f -> f.getBalance().getName().equalsIgnoreCase(resultMcc)).findFirst();
        return optional.map(accountBalanceJoin -> request.getTotalAmount().compareTo(accountBalanceJoin.getCurrentBalance()) <= 0).orElse(true);
    }


}
