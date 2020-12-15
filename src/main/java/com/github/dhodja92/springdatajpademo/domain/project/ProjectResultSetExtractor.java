package com.github.dhodja92.springdatajpademo.domain.project;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class ProjectResultSetExtractor implements ResultSetExtractor<List<Project>> {

    @Override
    public List<Project> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, Project> projectsById = new HashMap<>();

        while (rs.next()) {
            UUID id = rs.getObject("p_id", UUID.class);
            Project currentProject = projectsById.computeIfAbsent(id, t -> {
                try {
                    return new Project(
                            id,
                            rs.getString("p_name"),
                            rs.getString("p_color"),
                            new HashSet<>()
                    );
                } catch (SQLException e) {
                    throw new RecoverableDataAccessException("Error mapping Project", e);
                }
            });

            String taskIdString = rs.getString("t_id");
            if (taskIdString != null) {
                currentProject.addTask(
                        new Task(
                                UUID.fromString(taskIdString),
                                rs.getString("t_name"),
                                rs.getBoolean("t_finished"),
                                Label.valueOf(rs.getString("t_label")),
                                Priority.valueOf(rs.getString("t_priority"))
                        )
                );
            }
        }

        return new ArrayList<>(projectsById.values());
    }
}
