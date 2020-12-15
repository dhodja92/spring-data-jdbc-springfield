package com.github.dhodja92.springdatajpademo.infrastructure.jdbc;

public interface UpdateEntity<T> {

    T update(T entity);
}
