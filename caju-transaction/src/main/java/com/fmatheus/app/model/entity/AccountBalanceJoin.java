package com.fmatheus.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_balance_join")
public class AccountBalanceJoin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected AccountBalanceJoinPK pk;

    @Column(name = "current_balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "total_balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalBalance;

    @JoinColumn(name = "id_account", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;

    @JoinColumn(name = "id_balance", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Balance balance;

}
