package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.task.Task;
import com.github.dhodja92.springdatajpademo.domain.task.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping(path = "/api/tasks")
public class TaskResourceController {

    private final TaskRepository taskRepository;

    private final TaskResourceAssembler taskResourceAssembler;

    public TaskResourceController(
            TaskRepository taskRepository,
            TaskResourceAssembler taskResourceAssembler
    ) {
        Objects.requireNonNull(taskRepository, "taskRepository must not be null");
        Objects.requireNonNull(taskRepository, "taskResourceAssembler must not be null");
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<TaskResource>> getAllTasks(
            PagedResourcesAssembler<Task> pagedResourcesAssembler,
            Pageable pageable
    ) {
        Page<Task> tasks = this.taskRepository.findAll(pageable);
        PagedModel<TaskResource> resources = pagedResourcesAssembler.toModel(tasks, this.taskResourceAssembler);
        return ResponseEntity.ok(resources);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskResource> getTaskById(@PathVariable UUID id) {
        Task task = this.taskRepository.findById(id);
        if (task == null) {
            throw new NullPointerException();
        }
        TaskResource resource = this.taskResourceAssembler.toModel(task);
        return ResponseEntity.ok(resource);
    }
}
