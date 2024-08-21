package com.fmatheus.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document"}),
        @UniqueConstraint(columnNames = {"id"})})
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 70)
    private String name;

    @Column(name = "document", nullable = false, length = 20)
    private String document;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    private Address address;

    @JoinColumn(name = "id_person_type", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private PersonType personType;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    private Contact contact;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPerson")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPerson")
    private Establishment establishment;
}
