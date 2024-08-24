package com.fmatheus.app.model.service.impl;

import com.fmatheus.app.model.entity.Establishment;
import com.fmatheus.app.model.repository.EstablishmentRepository;
import com.fmatheus.app.model.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class EstablishmentServiceImpl implements EstablishmentService {

    private final EstablishmentRepository repository;

    @Override
    public List<Establishment> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Establishment save(Establishment establishment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Establishment> findById(UUID uuid) {
        return this.repository.findById(uuid);
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new UnsupportedOperationException();
    }

}
