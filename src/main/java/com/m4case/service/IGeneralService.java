package com.m4case.service;

import java.util.Optional;

public interface IGeneralService<T> {
    Iterable<T> findAll();

    Optional<T> findById(Long id);

    Object saveObj(T t);
    void save(T t);

    void delete(Long id);
}
