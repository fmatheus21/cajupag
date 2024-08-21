package com.fmatheus.app.model.service;


import com.fmatheus.app.model.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountService extends GenericService<Account, UUID> {

    Optional<Account> findByCardNumber(String cardNumber);

}
