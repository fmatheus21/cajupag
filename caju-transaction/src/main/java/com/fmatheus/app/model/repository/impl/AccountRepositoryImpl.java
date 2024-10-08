package com.fmatheus.app.model.repository.impl;

import com.fmatheus.app.model.entity.Account;
import com.fmatheus.app.model.repository.impl.restriction.AccountRestriction;
import com.fmatheus.app.model.repository.query.AccountRepositoryQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Optional;
import java.util.UUID;

public class AccountRepositoryImpl extends AccountRestriction implements AccountRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    /**
     * Consulta a conta do cliente atraves do identificador.
     * Estou utilizando Lock Pessimistic para que transacoes posteriores fiquem bloqueadas de ler e/ou escrever neste registro ate que a atual transacao seja concluida.
     * Isso garante a concsistencia dos dados em requisicoes concorrentes.
     *
     * @param account Identificador
     * @return {@link Optional<Account>}
     */
    @Override
    public Optional<Account> findByAccountPessimisticWriteLock(UUID account) {

        var builder = this.manager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = builder.createQuery(Account.class);
        Root<Account> root = criteriaQuery.from(Account.class);
        Predicate[] predicates = createRestrictions(builder, root, account);
        criteriaQuery
                .where(predicates);

        TypedQuery<Account> typedQuery = this.manager.createQuery(criteriaQuery).setLockMode(LockModeType.PESSIMISTIC_WRITE);

        return Optional.of(typedQuery.getSingleResult());
    }
}
