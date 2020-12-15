package com.github.dhodja92.springdatajpademo.infrastructure.jdbc.impl;

import com.github.dhodja92.springdatajpademo.infrastructure.jdbc.UpdateEntity;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;

public class UpdateEntityImpl<T> implements UpdateEntity<T> {

    private final JdbcAggregateOperations jdbcAggregateOperations;

    public UpdateEntityImpl(JdbcAggregateOperations jdbcAggregateOperations) {
        this.jdbcAggregateOperations = jdbcAggregateOperations;
    }

    @Override
    public T update(T entity) {
        return this.jdbcAggregateOperations.update(entity);
    }
}
