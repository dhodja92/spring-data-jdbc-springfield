package com.github.dhodja92.springdatajpademo.domain.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

public class ProjectRepositoryImpl implements PagingProjectRepository {

    private final JdbcOperations jdbcOperations;

    public ProjectRepositoryImpl(
            JdbcOperations jdbcOperations
    ) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        String fromJoinQueryPart = " FROM project p LEFT JOIN task t ON p.id = t.project_id";

        String selectQuery = "SELECT p.id p_id, p.name p_name, p.color p_color," +
                " t.id t_id, t.name t_name, t.finished t_finished, t.label t_label, t.priority t_priority" +
                fromJoinQueryPart +
                " OFFSET ? LIMIT ?";
        String countQuery = "SELECT COUNT(p.id) FROM project p";

        List<Project> projects = this.jdbcOperations.query(
                selectQuery,
                new ProjectRowMapper(),
                pageable.getPageNumber() * pageable.getPageSize(),
                pageable.getPageSize()
        );
        Long projectCount = this.jdbcOperations.queryForObject(countQuery, Long.class);

        return new PageImpl<>(
                projects,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                projectCount
        );
    }
}
