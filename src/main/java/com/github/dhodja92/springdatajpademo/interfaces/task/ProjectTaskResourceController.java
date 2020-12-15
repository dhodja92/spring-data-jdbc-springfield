package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import com.github.dhodja92.springdatajpademo.domain.task.TaskRepository;
import com.github.dhodja92.springdatajpademo.interfaces.project.ProjectResourceController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(path = "/api/projects")
public class ProjectTaskResourceController {

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final TaskResourceAssembler taskResourceAssembler;

    public ProjectTaskResourceController(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            TaskResourceAssembler taskResourceAssembler
    ) {
        Objects.requireNonNull(projectRepository, "projectRepository must not be null");
        Objects.requireNonNull(taskRepository, "taskRepository must not be null");
        Objects.requireNonNull(taskResourceAssembler, "taskResourceAssembler must not be null");
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @GetMapping(path = "/{id}/tasks")
    public ResponseEntity<PagedModel<TaskResource>> getProjectTasks(
            @PathVariable UUID id,
            PagedResourcesAssembler<Task> pagedResourcesAssembler,
            Pageable pageable
    ) {
        Page<Task> tasks = this.taskRepository.findByProjectId(id, pageable);
        PagedModel<TaskResource> resources = pagedResourcesAssembler.toModel(tasks, this.taskResourceAssembler);
        resources.add(linkTo(methodOn(ProjectResourceController.class).getProjectById(id)).withRel("project"));
        return ResponseEntity.ok(resources);
    }

    @PostMapping(path = "/{id}/tasks")
    public ResponseEntity<Void> createProjectTask(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProjectTaskRequest createProjectTaskRequest
    ) {
        Project project = this.projectRepository.findById(id)
                .orElseThrow(NullPointerException::new);
        project.addTask(createProjectTaskRequest.toTask());

        Task task = this.projectRepository.save(project).getTasks().stream()
                .filter(t -> Objects.equals(t.getName(), createProjectTaskRequest.getName()))
                .findFirst()
                .orElseThrow(NullPointerException::new);

        URI location = linkTo(methodOn(TaskResourceController.class).getTaskById(task.getId())).toUri();
        return ResponseEntity.created(location).build();
    }
}
