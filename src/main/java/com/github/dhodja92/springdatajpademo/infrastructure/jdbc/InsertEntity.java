package com.github.dhodja92.springdatajpademo.infrastructure.jdbc;

public interface InsertEntity<T> {

    T insert(T entity);
}
