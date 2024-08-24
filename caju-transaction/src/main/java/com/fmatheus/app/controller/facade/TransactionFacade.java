package com.fmatheus.app.controller.facade;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.enumerable.BalanceTypeEnum;
import com.fmatheus.app.controller.util.AppUtil;
import com.fmatheus.app.model.entity.*;
import com.fmatheus.app.model.service.AccountService;
import com.fmatheus.app.model.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionFacade {

    private boolean transactionApproved;
    private final Map<String, String> mapResponse = new HashMap<>();
    private BalanceTypeEnum balanceTypeEnum;
    private Balance balance;
    private final AccountService accountService;
    private final EstablishmentService establishmentService;


    public Map<String, String> startTransaction(TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {

        var claim = jwt.getClaims().get("account").toString();
        UUID account = UUID.fromString(claim);

        Account result = this.accountService.findByAccountPessimisticWriteLock(account).orElse(null);

        if (result == null) {
            return this.transactionUnknownError();
        }

        this.validateAccountBalance(request, result.getAccountBalanceJoinCollection());

        if (!this.validateEstablishment(request.getIdEstablishment())) {
            return transactionUnknownError();
        }

        return this.executeTransaction(request, result);

    }

    private Map<String, String> executeTransaction(TransactionRequest request, Account account) {
        Collection<AccountBalanceJoin> collection = this.updateAccountBalanceJoin(request, account.getAccountBalanceJoinCollection());
        account.getAccountBalanceJoinCollection().clear();
        account.getAccountBalanceJoinCollection().addAll(collection);

        Transactions transactions = Transactions.builder()
                .account(account)
                .amount(request.getTotalAmount())
                .status(this.transactionApproved ? "APROVADA" : "REJEITADA")
                .balance(this.balance)
                .establishment(Establishment.builder()
                        .id(request.getIdEstablishment())
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        account.getTransactionCollection().add(transactions);

        try {
            this.accountService.save(account);
        } catch (Exception ex) {
            return this.transactionUnknownError();
        }

        return this.transactionApproved ? transactionApproved() : transactionRejected();

    }

    private Collection<AccountBalanceJoin> updateAccountBalanceJoin(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        return collection.stream()
                .map(accountBalance -> {
                    if (accountBalance.getBalance().getName().equalsIgnoreCase(this.balanceTypeEnum.name())) {
                        this.balance = accountBalance.getBalance();
                        return AccountBalanceJoin.builder()
                                .pk(accountBalance.getPk())
                                .currentBalance(this.transactionApproved ? accountBalance.getCurrentBalance().subtract(request.getTotalAmount()) : accountBalance.getCurrentBalance())
                                .totalBalance(accountBalance.getTotalBalance())
                                .account(accountBalance.getAccount())
                                .balance(accountBalance.getBalance())
                                .build();
                    }
                    return accountBalance;
                })
                .toList();
    }


    private void validateAccountBalance(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        this.balanceTypeEnum = AppUtil.determineBalanceType(request.getMcc());
        var resultMcc = this.balanceTypeEnum.name();
        Optional<AccountBalanceJoin> optional = collection.stream().filter(f -> f.getBalance().getName().equalsIgnoreCase(resultMcc)).findFirst();
        this.transactionApproved = optional.map(accountBalanceJoin -> request.getTotalAmount().compareTo(accountBalanceJoin.getCurrentBalance()) <= 0).orElse(true);
    }

    private boolean validateEstablishment(UUID idEstablishment) {
        var establishment = this.establishmentService.findById(idEstablishment).orElse(null);

        if (establishment == null) {
            return false;
        }

        Optional<Balance> optional = establishment.getBalances().stream().filter(f -> f.getName().equalsIgnoreCase(this.balanceTypeEnum.name())).findFirst();

        return optional.isPresent();
    }


    private Map<String, String> transactionApproved() {
        this.mapResponse.put("code", "00");
        return this.mapResponse;
    }

    private Map<String, String> transactionRejected() {
        this.mapResponse.put("code", "51");
        return this.mapResponse;
    }

    private Map<String, String> transactionUnknownError() {
        this.mapResponse.put("code", "07");
        return this.mapResponse;
    }


}
