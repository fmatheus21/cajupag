package com.fmatheus.app.model.repository.impl.restriction;

import com.fmatheus.app.model.entity.Account;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AccountRestriction {

    protected Predicate[] createRestrictions(CriteriaBuilder builder, Root<Account> root, UUID account) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.<UUID>get("id"), account));
        return predicates.toArray(new Predicate[0]);

    }

}
