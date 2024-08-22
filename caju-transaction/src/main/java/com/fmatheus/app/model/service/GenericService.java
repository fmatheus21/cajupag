package com.fmatheus.app.model.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    List<T> findAll();

    T save(T t);

    Optional<T> findById(ID id);

    void deleteById(ID id);

}