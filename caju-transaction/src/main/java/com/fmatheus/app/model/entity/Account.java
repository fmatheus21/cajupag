package com.fmatheus.app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}),
        @UniqueConstraint(columnNames = {"card_number"})})
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "card_number", nullable = false, length = 20)
    private String cardNumber;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<AccountBalanceJoin> accountBalanceJoinCollection;

    @JoinColumn(name = "id_client", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<Transactions> transactionCollection;

}
