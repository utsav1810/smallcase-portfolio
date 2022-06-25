package com.smallcase.portfolio.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, I> {
    Optional<T> get(int id);
    List<T> getAll();
    Optional<I> save(T t);
    void update(T t);
    void delete(T t);
}