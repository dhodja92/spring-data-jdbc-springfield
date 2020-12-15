package com.github.dhodja92.springdatajpademo.domain.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("t_id", UUID.class);
        try {
            return new Task(
                    id,
                    rs.getString("t_name"),
                    rs.getBoolean("t_finished"),
                    Label.valueOf(rs.getString("t_label")),
                    Priority.valueOf(rs.getString("t_priority"))
            );
        } catch (SQLException e) {
            throw new RecoverableDataAccessException("Error mapping Task", e);
        }
    }
}
