package com.github.dhodja92.springdatajpademo.domain.task;

import com.github.dhodja92.springdatajpademo.infrastructure.jdbc.InsertEntity;
import com.github.dhodja92.springdatajpademo.infrastructure.jdbc.UpdateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends Repository<Task, UUID>, InsertEntity<Task>, UpdateEntity<Task> {

    Task findById(UUID id);

    boolean existsById(UUID id);

    Page<Task> findAll(Pageable pageable);

    @Query(
            value = "SELECT t.* FROM task t JOIN project p ON p.id = t.project_id WHERE p.id = :projectId",
            resultSetExtractorClass = TaskResultSetExtractor.class
    )
    Page<Task> findByProjectId(UUID projectId, Pageable pageable);

    @Query(
            value = "SELECT * FROM task t JOIN project p ON p.id = t.project_id WHERE p.id = :projectId",
            resultSetExtractorClass = TaskResultSetExtractor.class
    )
    List<Task> findByProjectId(UUID projectId);

    Page<Task> findByName(String name, Pageable pageable);

    Task save(Task task);

    void delete(Task task);
}
