package com.fmatheus.app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})})
public class Transactions implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 70)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "id_account", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Account account;

    @JoinColumn(name = "id_balance", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Balance balance;

    @JoinColumn(name = "id_establishment", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Establishment establishment;

}