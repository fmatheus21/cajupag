package com.fmatheus.app.model.repository.query;

import com.fmatheus.app.model.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryQuery {

    Optional<Account> findByAccountPessimisticWriteLock(UUID id);
}
