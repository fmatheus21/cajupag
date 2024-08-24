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
@Table(name = "establishment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_person"}),
        @UniqueConstraint(columnNames = {"id"})})
public class Establishment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "merchant", length = 70, nullable = false)
    private String merchant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "id_person", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private Person idPerson;

    @JoinTable(name = "establishment_balance_join", joinColumns = @JoinColumn(name = "id_establishmen"), inverseJoinColumns = @JoinColumn(name = "id_balance"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Balance> balances;

}