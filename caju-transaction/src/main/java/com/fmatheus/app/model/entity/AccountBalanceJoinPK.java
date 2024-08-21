package com.fmatheus.app.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AccountBalanceJoinPK implements Serializable {

    @Column(name = "id_account", nullable = false)
    private UUID idAccount;

    @Column(name = "id_balance", nullable = false)
    private int idBalance;

}