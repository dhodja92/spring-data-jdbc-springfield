package com.github.dhodja92.springdatajpademo.domain.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class TaskResultSetExtractor implements ResultSetExtractor<List<Task>> {

    @Override
    public List<Task> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, Task> tasksById = new HashMap<>();

        while (rs.next()) {
            UUID id = rs.getObject("id", UUID.class);
            tasksById.computeIfAbsent(id, t -> {
                try {
                    return new Task(
                            id,
                            rs.getString("name"),
                            rs.getBoolean("finished"),
                            Label.valueOf(rs.getString("label")),
                            Priority.valueOf(rs.getString("priority"))
                    );
                } catch (SQLException e) {
                    throw new RecoverableDataAccessException("Error mapping Project", e);
                }
            });
        }

        return new ArrayList<>(tasksById.values());
    }
}
