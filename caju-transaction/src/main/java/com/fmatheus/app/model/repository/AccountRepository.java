package com.fmatheus.app.model.repository;

import com.fmatheus.app.model.entity.Account;
import com.fmatheus.app.model.repository.query.AccountRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, AccountRepositoryQuery {
}
