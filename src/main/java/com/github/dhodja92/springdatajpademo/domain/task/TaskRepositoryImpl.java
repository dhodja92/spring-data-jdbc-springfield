package com.github.dhodja92.springdatajpademo.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TaskRepositoryImpl implements PagingTaskRepository {

    private final JdbcOperations jdbcOperations;

    public TaskRepositoryImpl(
            JdbcOperations jdbcOperations
    ) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        String fromJoinWhereQueryPart = " FROM task t";

        String selectQuery = "SELECT t.id t_id, t.name t_name, t.finished t_finished, t.label t_label, t.priority t_priority" +
                fromJoinWhereQueryPart +
                " OFFSET ? LIMIT ?";
        String countQuery = "SELECT COUNT(t.id) FROM task t";

        List<Task> tasks = this.jdbcOperations.query(
                selectQuery,
                new TaskRowMapper(),
                pageable.getPageNumber() * pageable.getPageSize(),
                pageable.getPageSize()
        );
        Long taskCount = this.jdbcOperations.queryForObject(countQuery, Long.class);

        return new PageImpl<>(
                tasks,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                taskCount
        );
    }

    @Override
    public Page<Task> findByProjectId(UUID projectId, Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        String fromJoinWhereQueryPart = " FROM task t WHERE t.project_id = ?";

        String selectQuery = "SELECT t.id t_id, t.name t_name, t.finished t_finished, t.label t_label, t.priority t_priority" +
                fromJoinWhereQueryPart +
                " OFFSET ? LIMIT ?";
        String countQuery = "SELECT COUNT(t.id)" + fromJoinWhereQueryPart;

        List<Task> tasks = this.jdbcOperations.query(
                selectQuery,
                new TaskRowMapper(),
                projectId,
                pageable.getPageNumber() * pageable.getPageSize(),
                pageable.getPageSize()
        );
        Long taskCount = this.jdbcOperations.queryForObject(
                countQuery,
                Long.class,
                projectId
        );

        return new PageImpl<>(
                tasks,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                taskCount
        );
    }
}
