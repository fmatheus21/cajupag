package com.fmatheus.app.controller.facade;

import com.fmatheus.app.controller.dto.request.TransactionRequest;
import com.fmatheus.app.model.entity.*;
import com.fmatheus.app.model.service.AccountService;
import com.fmatheus.app.model.service.EstablishmentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste de Transação")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TransactionFacadeTest {

    private final BigDecimal APPROVED_VALUE = new BigDecimal("20.0");
    private final BigDecimal REJECTED_VALUE = new BigDecimal("50000.0");

    @Mock
    private AccountService accountService;

    @Mock
    private EstablishmentService establishmentService;

    @Mock
    private Balance balance;

    /**
     * Test: {@link TransactionFacade#startTransaction(TransactionRequest, Jwt)}
     */
    @Test
    @Order(1)
    @DisplayName("Transação Aprovada.")
    void testStartTransactionApproved() {

        var transactionFacadeUnderTest = new TransactionFacade(this.accountService, this.establishmentService);
        ReflectionTestUtils.setField(transactionFacadeUnderTest, "balance", this.balance);

        var request = this.loadTransactionRequest();
        request.setTotalAmount(APPROVED_VALUE);

        var jwt = this.loadToken();

        var expectedResult = Map.ofEntries(Map.entry("code", "00"));

        var account = this.loadAccount();

        when(this.accountService.findByAccountPessimisticWriteLock(UUID.fromString("0015dc51-05d5-11ee-900d-7085c2be6d69"))).thenReturn(account);

        var establishment = this.loadEstablishmentFood();

        when(this.establishmentService.findById(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))).thenReturn(establishment);

        var result = transactionFacadeUnderTest.startTransaction(request, jwt);

        assertThat(result).isEqualTo(expectedResult);
        verify(this.accountService).save(any(Account.class));
    }

    /**
     * Test: {@link TransactionFacade#startTransaction(TransactionRequest, Jwt)}
     */
    @Test
    @Order(2)
    @DisplayName("Transação Rejeitada por falta de saldo.")
    void testStartTransactionRejected() {

        var transactionFacadeUnderTest = new TransactionFacade(this.accountService, this.establishmentService);
        ReflectionTestUtils.setField(transactionFacadeUnderTest, "balance", this.balance);

        var request = this.loadTransactionRequest();
        request.setTotalAmount(REJECTED_VALUE);

        var jwt = this.loadToken();

        var expectedResult = Map.ofEntries(Map.entry("code", "51"));

        var account = this.loadAccount();

        when(this.accountService.findByAccountPessimisticWriteLock(UUID.fromString("0015dc51-05d5-11ee-900d-7085c2be6d69"))).thenReturn(account);

        var establishment = this.loadEstablishmentFood();

        when(this.establishmentService.findById(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))).thenReturn(establishment);

        var result = transactionFacadeUnderTest.startTransaction(request, jwt);

        assertThat(result).isEqualTo(expectedResult);
        verify(this.accountService).save(any(Account.class));
    }

    /**
     * Test: {@link TransactionFacade#startTransaction(TransactionRequest, Jwt)}
     */
    @Test
    @Order(3)
    @DisplayName("Erro na transação. Tipo de produto não vinculado ao estabelecimento.")
    void testStartTransactionUnknownError() {

        var transactionFacadeUnderTest = new TransactionFacade(this.accountService, this.establishmentService);
        ReflectionTestUtils.setField(transactionFacadeUnderTest, "balance", this.balance);

        var request = this.loadTransactionRequest();
        request.setTotalAmount(APPROVED_VALUE);

        var jwt = this.loadToken();

        var expectedResult = Map.ofEntries(Map.entry("code", "07"));

        var account = this.loadAccount();

        when(this.accountService.findByAccountPessimisticWriteLock(UUID.fromString("0015dc51-05d5-11ee-900d-7085c2be6d69"))).thenReturn(account);

        var establishment = this.loadEstablishmentMeal();

        when(this.establishmentService.findById(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))).thenReturn(establishment);

        var result = transactionFacadeUnderTest.startTransaction(request, jwt);

        assertThat(result).isEqualTo(expectedResult);

    }


    private Jwt loadToken() {
        var localDateTime = LocalDateTime.now();
        return new Jwt(
                "tokenValue",
                localDateTime.toInstant(ZoneOffset.UTC),
                localDateTime.plusDays(2).toInstant(ZoneOffset.UTC),
                Map.ofEntries(Map.entry("card_number", "**** **** **** 7322")),
                Map.ofEntries(Map.entry("account", "0015dc51-05d5-11ee-900d-7085c2be6d69"))
        );
    }

    private TransactionRequest loadTransactionRequest() {
        return TransactionRequest.builder()
                .totalAmount(new BigDecimal("0.0"))
                .mcc("5411")
                .merchant("MS RESTAURANTE - SAO PAULO BR")
                .idEstablishment(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))
                .build();
    }

    private Optional<Account> loadAccount() {
        return Optional.of(Account.builder()
                .accountBalanceJoinCollection(Collections.singleton(AccountBalanceJoin.builder()
                        .pk(AccountBalanceJoinPK.builder().build())
                        .currentBalance(new BigDecimal("1000.00"))
                        .totalBalance(new BigDecimal("1000.00"))
                        .balance(Balance.builder()
                                .id(1)
                                .name("FOOD")
                                .build())
                        .build()))
                .transactionCollection(new ArrayList<>())
                .build());
    }

    private Optional<Establishment> loadEstablishmentFood() {
        return Optional.of(Establishment.builder()
                .id(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))
                .merchant("MS RESTAURANTE - SAO PAULO BR")
                .balances(List.of(Balance.builder()
                        .id(1)
                        .name("FOOD")
                        .build()))
                .build());
    }

    private Optional<Establishment> loadEstablishmentMeal() {
        return Optional.of(Establishment.builder()
                .id(UUID.fromString("e003f62a-5e8c-11ef-9650-581122c7752d"))
                .merchant("MS RESTAURANTE - SAO PAULO BR")
                .balances(List.of(Balance.builder()
                        .id(1)
                        .name("MEAL")
                        .build()))
                .build());
    }

}
