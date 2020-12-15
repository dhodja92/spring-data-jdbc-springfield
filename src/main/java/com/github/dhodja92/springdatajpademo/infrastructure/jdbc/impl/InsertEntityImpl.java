package com.github.dhodja92.springdatajpademo.infrastructure.jdbc.impl;

import com.github.dhodja92.springdatajpademo.infrastructure.jdbc.InsertEntity;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;

public class InsertEntityImpl<T> implements InsertEntity<T> {

    private final JdbcAggregateOperations jdbcAggregateOperations;

    public InsertEntityImpl(JdbcAggregateOperations jdbcAggregateOperations) {
        this.jdbcAggregateOperations = jdbcAggregateOperations;
    }

    @Override
    public T insert(T entity) {
        return this.jdbcAggregateOperations.insert(entity);
    }
}
