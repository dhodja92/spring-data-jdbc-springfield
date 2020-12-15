package com.github.dhodja92.springdatajpademo.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID>, PagingTaskRepository {

    boolean existsById(UUID id);

    Page<Task> findAll(Pageable pageable);

    @Query(
            value = "SELECT * FROM task t JOIN project p ON p.id = t.project_id WHERE p.id = :projectId",
            resultSetExtractorClass = TaskResultSetExtractor.class
    )
    List<Task> findByProjectId(UUID projectId);

    Page<Task> findByName(String name, Pageable pageable);
}
