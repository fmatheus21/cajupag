package com.fmatheus.app.model.service.impl;

import com.fmatheus.app.model.entity.Account;
import com.fmatheus.app.model.repository.AccountRepository;
import com.fmatheus.app.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public Account save(Account account) {
        return this.repository.save(account);
    }

    @Override
    public Optional<Account> findById(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new UnsupportedOperationException();
    }


    @Transactional
    @Override
    public Optional<Account> findByAccountPessimisticWriteLock(UUID id) {
        return this.repository.findByAccountPessimisticWriteLock(id);
    }
}
