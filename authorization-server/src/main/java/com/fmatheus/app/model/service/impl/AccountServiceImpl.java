package com.fmatheus.app.model.service.impl;

import com.fmatheus.app.controller.util.CharacterUtil;
import com.fmatheus.app.model.entity.Account;
import com.fmatheus.app.model.repoitory.AccountRepository;
import com.fmatheus.app.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public Optional<Account> findByCardNumber(String cardNumber) {
        return this.repository.findByCardNumber(CharacterUtil.removeSpecialCharacters(cardNumber));
    }

    @Override
    public List<Account> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Account> findById(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Account save(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new UnsupportedOperationException();
    }
}
