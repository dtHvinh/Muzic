package com.dthvinh.Server.Repositories.Contract;

import java.util.List;
import java.util.Optional;

public interface Repository<T, TKey> {
    T save(T a);

    List<T> findAll(String search, Integer limit, Integer offset);

    Optional<T> findById(TKey id);

    T update(TKey id, T a);

    boolean delete(TKey id);
}
