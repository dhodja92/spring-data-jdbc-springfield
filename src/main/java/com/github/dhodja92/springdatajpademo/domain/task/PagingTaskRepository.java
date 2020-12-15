package com.github.dhodja92.springdatajpademo.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PagingTaskRepository {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByProjectId(UUID projectId, Pageable pageable);
}
