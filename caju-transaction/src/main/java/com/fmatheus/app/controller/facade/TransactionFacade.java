package com.fmatheus.app.controller.facade;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.controller.enumerable.BalanceTypeEnum;
import com.fmatheus.app.controller.util.AppUtil;
import com.fmatheus.app.model.entity.*;
import com.fmatheus.app.model.service.AccountService;
import com.fmatheus.app.model.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionFacade {

    private boolean transactionApproved;
    private BalanceTypeEnum balanceTypeEnum;
    private Balance balance;
    private final AccountService accountService;
    private final EstablishmentService establishmentService;


    public Map<String, String> startTransaction(TransactionRequest request, Jwt jwt) {

        Map<String, String> responseMap = new HashMap<>();

        var claim = jwt.getClaims().get("account").toString();
        UUID account = UUID.fromString(claim);

        Account result = this.accountService.findByAccountPessimisticWriteLock(account).orElse(null);

        if (result == null) {
            return this.transactionUnknownError(responseMap);
        }

        this.validateAccountBalance(request, result);

        if (!this.validateEstablishment(request.getIdEstablishment())) {
            return transactionUnknownError(responseMap);
        }

        return this.executeTransaction(request, result, responseMap);

    }

    /**
     * Executa a transacao.
     *
     * @param request Objeto enviado na requisicao
     * @param account Conta do cliente
     * @return {@link Map}
     */
    private Map<String, String> executeTransaction(TransactionRequest request, Account account, Map<String, String> responseMap) {

        this.updateAccountBalanceJoin(request, account.getAccountBalanceJoinCollection());

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
            return this.transactionUnknownError(responseMap);
        }

        return this.transactionApproved ? transactionApproved(responseMap) : transactionRejected(responseMap);

    }

    private void updateAccountBalanceJoin(TransactionRequest request, Collection<AccountBalanceJoin> collection) {
        for (AccountBalanceJoin accountBalance : collection) {
            if (accountBalance.getBalance().getName().equalsIgnoreCase(this.balanceTypeEnum.name())) {
                this.balance = accountBalance.getBalance();
                BigDecimal amount = this.transactionApproved ? accountBalance.getCurrentBalance().subtract(request.getTotalAmount()) : accountBalance.getCurrentBalance();
                accountBalance.setCurrentBalance(amount);
                accountBalance.setTotalBalance(accountBalance.getTotalBalance());
                accountBalance.setAccount(accountBalance.getAccount());
                accountBalance.setBalance(accountBalance.getBalance());
            }
        }
    }


    /**
     * Obtem o tipo de saldo atraves do MCC.
     * Percorre a collection de AccountBalanceJoin e filtra pelo tipo de saldo obtido pelo MCC.
     * Se o valor enviado na requisicao(totalAmount) for igual ou menor ao saldo da conta, atribui o valor 'true' a variavel transactionApproved.
     *
     * @param request Objeto enviado na requisicao
     * @param account Conta do cliente
     */
    private void validateAccountBalance(TransactionRequest request, Account account) {
        this.balanceTypeEnum = AppUtil.determineBalanceType(request.getMcc());
        var resultMcc = this.balanceTypeEnum.name();
        Optional<AccountBalanceJoin> optional = account.getAccountBalanceJoinCollection().stream().filter(f -> f.getBalance().getName().equalsIgnoreCase(resultMcc)).findFirst();
        this.transactionApproved = optional.map(accountBalanceJoin -> request.getTotalAmount().compareTo(accountBalanceJoin.getCurrentBalance()) <= 0).orElse(true);
    }

    /**
     * Verifica se o estabelecimento existe. Se existir, verifica se o estabelecimento esta de acordo com o tipo de produto vendido.
     *
     * @param idEstablishment Identificador do estabelecimento
     * @return boolean
     */
    private boolean validateEstablishment(UUID idEstablishment) {
        var establishment = this.establishmentService.findById(idEstablishment).orElse(null);

        if (establishment == null) {
            return false;
        }

        Optional<Balance> optional = establishment.getBalances().stream().filter(f -> f.getName().equalsIgnoreCase(this.balanceTypeEnum.name())).findFirst();

        return optional.isPresent();
    }

    /**
     * Retorna resposta de sucesso para transacao aprovada.
     *
     * @return {@link Map<>}
     */
    private Map<String, String> transactionApproved(Map<String, String> responseMap) {
        responseMap.put("code", "00");
        return responseMap;
    }

    /**
     * Retorna resposta de erro de transacao rejeitada.
     *
     * @return {@link Map<>}
     */
    private Map<String, String> transactionRejected(Map<String, String> responseMap) {
        responseMap.put("code", "51");
        return responseMap;
    }

    /**
     * Retorna resposta de erro nao reconhecido.
     *
     * @return {@link Map<>}
     */
    private Map<String, String> transactionUnknownError(Map<String, String> responseMap) {
        responseMap.put("code", "07");
        return responseMap;
    }


}
