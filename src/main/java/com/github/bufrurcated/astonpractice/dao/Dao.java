package com.github.bufrurcated.astonpractice.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T, K> {
    void save(T t) throws SQLException;
    List<T> findAll() throws SQLException;
    List<T> find(K k) throws SQLException;
    void update(T t) throws SQLException;
    void delete(K k) throws SQLException;
    void deleteAll() throws SQLException;
}
